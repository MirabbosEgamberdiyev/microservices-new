FROM openjdk:17
WORKDIR /app
COPY target/order-service.jar order-service.jar
ENTRYPOINT ["java", "-jar", "order-service.jar"]
EXPOSE 8082
