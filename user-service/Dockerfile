# Stage 1: Build bằng Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM ubuntu/jre:21-24.04_stable
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]