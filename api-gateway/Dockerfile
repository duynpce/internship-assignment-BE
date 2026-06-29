# Stage 1: Build with Maven (with dependency caching)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy pom.xml first and pre-download dependencies as a separate layer.
# This layer is cached and only re-runs when pom.xml changes — not on every code change.
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Now copy source and build. This layer reruns only when source changes.
COPY src ./src
RUN mvn package -DskipTests -q

# Stage 2: Extract layered jar (Spring Boot 2.3+ feature)
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /extract
COPY --from=builder /build/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as non-root for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy layers in order of least-to-most frequently changed.
# Docker caches each COPY as a separate layer — dependencies rarely change,
# so those layers are almost always cache hits on rebuild.
COPY --from=extractor /extract/dependencies/ ./
COPY --from=extractor /extract/spring-boot-loader/ ./
COPY --from=extractor /extract/snapshot-dependencies/ ./
COPY --from=extractor /extract/application/ ./

EXPOSE 8000
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]