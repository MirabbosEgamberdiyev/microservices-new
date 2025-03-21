FROM openjdk:17
WORKDIR /app
COPY target/inventory-service.jar inventory-service.jar
ENTRYPOINT ["java", "-jar", "inventory-service.jar"]
EXPOSE 8083
