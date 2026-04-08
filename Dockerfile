# ===== Stage 1: Build (JDK) =====
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle* ./
COPY . .

ARG MODULE_NAME

RUN chmod +x ./gradlew
RUN ./gradlew :${MODULE_NAME}:bootJar -x test --no-daemon

# ---------------------------------------------------------

# ===== Stage 2: Runtime (JRE) =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ARG MODULE_NAME

# Create non-root user and group for security
RUN addgroup -S app && adduser -S app -G app

COPY --from=builder /app/${MODULE_NAME}/build/libs/*.jar app.jar

RUN chown app:app app.jar

USER app:app

ENV TZ=America/Sao_Paulo

ENTRYPOINT ["java", "-jar", "app.jar"]