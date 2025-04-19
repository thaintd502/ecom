#   deploy
FROM maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build /target/*.jar demo.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "demo.jar"]

#   chay docker
# FROM maven:3-eclipse-temurin-17 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests
#
# FROM eclipse-temurin:17-alpine
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar
# EXPOSE 9090
# ENTRYPOINT ["java", "-jar", "app.jar"]

