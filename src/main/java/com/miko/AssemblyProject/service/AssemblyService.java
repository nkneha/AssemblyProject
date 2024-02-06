package com.miko.AssemblyProject.service;

import com.miko.AssemblyProject.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssemblyService {

    private final Logger logger = LoggerFactory.getLogger(AssemblyService.class);

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<String, Integer> registers = new HashMap<>();

    @Autowired
    public AssemblyService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String executeAssemblyProgram(String program) {
        try {
            String[] statements = program.split("\\n");

            for (String statement : statements) {
                executeInstruction(statement.trim().split("\\s+"));
            }

            saveResultsToRedis();

            return "success";
        } catch (Exception e) {
            logger.error("Error executing assembly program:", e);
            return "failure";
        }
    }

    private void executeInstruction(String[] tokens) {
        if (tokens.length < 2) {
            System.out.println("Invalid input. Please provide an operation and a register.");
            return;
        }

        Operation operation = null;
        String register = tokens[1];

        if (tokens.length > 2) {
            try {
                int value = Integer.parseInt(tokens[2]);
                operation = Operation.valueOf(tokens[0].toUpperCase());

                switch (operation) {
                    case MV:
                        moveValue(register, value);
                        break;
                    case ADD:
                        addValue(register, value);
                        break;
                    case SHOW:
                        showValue(register);
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please provide a valid numeric value for the operation.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid operation. Please use MV, ADD, SHOW, or EXIT.");
            }
        } else {
            if ("SHOW".equalsIgnoreCase(tokens[0])) {
                showValue(register);
            } else {
                System.out.println("Invalid input. Please provide all required parameters.");
            }
        }
    }


    private void moveValue(String register, int value) {
        registers.put(register, value);
        logger.info("Moved value {} to register {}", value, register);
    }

    private void addValue(String register, int value) {
        int currentValue = registers.getOrDefault(register, 0);
        int newValue = currentValue + value;
        registers.put(register, newValue);
        logger.info("Added value {} to register {}", value, register);
    }

    private void showValue(String register) {
        int value = registers.getOrDefault(register, 0);
        logger.info("Value in register {}: {}", register, value);
    }

    private void saveResultsToRedis() {
        // Save the registers to Redis
        redisTemplate.opsForHash().putAll("registers", registers);
        logger.info("Results saved to Redis.");
    }
}
