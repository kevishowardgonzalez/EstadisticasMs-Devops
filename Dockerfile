# Etapa 1: Compilacion con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera final de ejecucion con usuario no-root
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]