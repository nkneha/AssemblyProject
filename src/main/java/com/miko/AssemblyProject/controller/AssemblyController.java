package com.miko.AssemblyProject.controller;

import com.miko.AssemblyProject.model.AssemblyRequest;
import com.miko.AssemblyProject.service.AssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssemblyController {

@Autowired
    private final AssemblyService assemblyService;

    @Autowired
    public AssemblyController(AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @PostMapping("/execute")
    public String executeAssemblyProgram(@RequestBody AssemblyRequest request) {
        return assemblyService.executeAssemblyProgram(request.getProgram());
    }

}
