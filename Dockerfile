FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ecom2-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
CMD ["java", "-jar", "app.jar"]
