# Microservicio Estadísticas - DevOps (Evaluación Parcial N°2)

Este repositorio contiene la arquitectura de integración y entrega continua (CI/CD) para el microservicio **estadisticasms**, desarrollado en **Java 21 con Spring Boot 3.2.5** y gestionado con **Apache Maven**.

---

## 👥 Datos del Estudiante
- **Estudiante:** Kevis Howard González
- **Asignatura:** Ingeniería DevOps (DOY0101)
- **Institución:** Duoc UC

---

## 1. Arquitectura y Contenerización (IE1)
El microservicio se encuentra empaquetado utilizando un `Dockerfile` multi-stage optimizado y seguro, complementado con `.dockerignore` para excluir archivos innecesarios del contexto de construcción:
- **Etapa de compilación (Build):** Emplea `maven:3.9.6-eclipse-temurin-21` para compilar y empaquetar el artefacto JAR ejecutable.
- **Etapa de ejecución (Runtime):** Utiliza `eclipse-temurin:21-jre` configurando un usuario sin privilegios (`appuser` en `appgroup`), eliminando la ejecución como `root` y exponiendo el puerto `8082`.

---

## 2. Pruebas Automatizadas y Cobertura JaCoCo (IE2)
- **Framework de Pruebas:** Se implementaron pruebas unitarias con **JUnit 5** y **Mockito**, aislando las capas de Controlador, Servicio, Entidad JPA y DTOs sin requerir una base de datos activa durante la fase de pruebas unitarias.
- **JaCoCo Plugin y Bloqueo por Cobertura:** Integrado en el `pom.xml` para generar el reporte XML (`target/site/jacoco/jacoco.xml`) y validar el umbral mínimo obligatorio mediante la meta `jacoco:check` durante la fase `verify`.
- **Configuración de Lombok:** Se configuró `lombok.config` (`lombok.addLombokGeneratedAnnotation = true`) para evitar que JaCoCo penalice métodos generados automáticamente (`equals`, `hashCode`, `toString`).

---

## 3. Análisis Estático de Calidad y Seguridad (IE3)
El pipeline incorpora controles reales y bloqueantes de calidad de código y gestión de vulnerabilidades:
- **SonarCloud (Calidad y Confiabilidad):**
  * Vinculación directa con el reporte de JaCoCo mediante `sonar.coverage.jacoco.xmlReportPaths`.
  * **Quality Gate bloqueante:** Ejecutado con `-Dsonar.qualitygate.wait=true`, deteniendo el pipeline si no se cumplen las métricas de confiabilidad, seguridad y cobertura.
- **Snyk y Dependabot (Seguridad en Dependencias):**
  * **Snyk CLI:** Valida la existencia de `SNYK_TOKEN` y ejecuta `snyk test --severity-threshold=high` (bloqueante ante vulnerabilidades altas o críticas, sin omitir errores), subiendo el reporte JSON como artefacto.
  * **Dependabot:** Configurado en `.github/dependabot.yml` para auditar actualizaciones semanales de dependencias Maven y acciones de GitHub Actions.

---

## 4. Pipeline de CI/CD en GitHub Actions (IE4)
El archivo `.github/workflows/ci.yml` automatiza el ciclo de vida del software ante eventos de `push` y `pull_request` sobre las ramas `develop` y `main` mediante 5 jobs secuenciales (`needs`):
1. **Pruebas y Cobertura (`test-and-coverage`):** Ejecuta `mvn clean verify` (con `jacoco:check`) y publica el reporte de JaCoCo como artefacto.
2. **Escaneo de Seguridad (`security-scan`):** Audita las dependencias con Snyk bloqueando severidades altas.
3. **Análisis SonarCloud (`sonarcloud-analysis`):** Procesa las métricas de código y exige la aprobación del Quality Gate.
4. **Construcción de Imagen Docker (`docker-build`):** Construye la imagen multi-stage etiquetándola con el SHA del commit (`estadisticas-ms:${{ github.sha }}`) para garantizar trazabilidad.
5. **Despliegue Simulado (`deploy-simulado`):** Levanta el stack completo con Docker Compose en el runner, verifica el estado saludable de ambos contenedores y ejecuta una prueba *smoke test* real (`curl -f http://localhost:8082/api/estadisticas`).

---

## 5. Orquestación y Despliegue Local con Docker Compose (IE5)
En el archivo `docker-compose.yml` se orquestan los servicios bajo una red personalizada (`red-estadisticas`), política de reinicio (`restart: unless-stopped`) y variables externalizadas en `.env` (a partir de `.env.example`):
- **Servicio de Base de Datos (`mariadb`):** MariaDB 11.4 con persistencia en `mariadb_data`, comprobación de salud (`healthcheck`) y montaje de los scripts SQL (`./src/main/resources/db_estadisticas:/docker-entrypoint-initdb.d:ro`) para crear e inicializar la tabla `estadisticas` automáticamente al arrancar.
- **Microservicio Java (`estadisticas-ms`):** Configurado con `depends_on` (`condition: service_healthy`), su propio `healthcheck` HTTP sobre `/api/estadisticas` y límites de CPU y memoria (`deploy.resources`).

### Pasos para levantar el stack localmente
1. **Copiar variables de entorno:**
   ```bash
   cp .env.example .env