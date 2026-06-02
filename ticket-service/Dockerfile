
# Stage 1: Build bằng Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy toàn bộ code vào trong image builder
COPY . .

# Chạy lệnh build ra file .jar bên trong Docker luôn
RUN mvn clean package -DskipTests

# Stage 2: Tạo image runtime siêu nhẹ để chạy
FROM ubuntu/jre:21-24.04_stable
WORKDIR /app

# Lấy file .jar từ Stage 1 qua Stage 2 (không lấy code thừa)
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]