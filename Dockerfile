FROM openjdk:17
WORKDIR /app
COPY ./target/AssemblyProject-0.0.1-SNAPSHOT.jar /app/AssemblyProject-0.0.1-SNAPSHOT.jar
EXPOSE 8080
#ARG JAR_FILE=target/*.jar
#COPY ./target/AssemblyProject-0.0.1-SNAPSHOT.jar AssemblyProject.jar
CMD ["java","-jar","AssemblyProject-0.0.1-SNAPSHOT.jar"]
