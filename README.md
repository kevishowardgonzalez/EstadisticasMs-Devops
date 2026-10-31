# Microservicio Estadísticas - DevOps (Evaluación Parcial N°2)

Este repositorio contiene la arquitectura de integración y entrega continua (CI/CD) para el microservicio **estadisticasms**, desarrollado en **Java 21 con Spring Boot 3.2.5** y gestionado con **Apache Maven**.

---

## 👥 Datos del Estudiante
* **Estudiante:** Kevis Howard González
* **Asignatura:** Ingeniería DevOps (DOY0101)
* **Institución:** Duoc UC

---

## 1. Arquitectura y Contenerización (IE1)
El microservicio se encuentra empaquetado utilizando un `Dockerfile` multi-stage optimizado:
* **Etapa de compilación (Build):** Emplea una imagen base de OpenJDK 21 con Maven para compilar y empaquetar el artefacto JAR ejecutable.
* **Etapa de ejecución (Runtime):** Utiliza una imagen ligera de Java 21 JRE (`eclipse-temurin:21-jre-alpine`), reduciendo la superficie de ataque y minimizando el peso del contenedor para despliegues en la nube.

---

## 2. Pruebas Automatizadas y Cobertura JaCoCo (IE2)
* **Framework de Pruebas:** Se implementaron pruebas unitarias con **JUnit 5** y **Mockito**, aislando las capas de Controlador, Servicio, Entidad JPA y DTOs sin requerir una base de datos activa durante la fase de CI.
* **JaCoCo Plugin:** Integrado en el `pom.xml` para generar el reporte de cobertura de código en formato XML (`target/site/jacoco/jacoco.xml`).
* **Configuración de Lombok:** Se configuró `lombok.config` (`lombok.addLombokGeneratedAnnotation = true`) para evitar que JaCoCo penalice métodos generados automáticamente (`equals`, `hashCode`, `toString`).

---

## 3. Análisis Estático de Calidad y Seguridad (IE3)
El pipeline incorpora un análisis integral de calidad de código y gestión de vulnerabilidades:
* **SonarCloud (Calidad y Confiabilidad):**
  * Vinculación directa con el reporte generado por JaCoCo mediante `sonar.coverage.jacoco.xmlReportPaths`.
  * **Corrección de regla java:S5863:** Se eliminaron las aserciones autorreferenciales (`assertEquals(obj, obj)`) en las pruebas unitarias, elevando el **Reliability Rating a calificación A**.
  * **Quality Gate:** Estado **Passed** (verde), asegurando estándares de mantenibilidad, seguridad y cobertura.
* **Snyk (Seguridad en Dependencias):**
  * Escaneo automático de dependencias en cada ejecución para detectar y prevenir vulnerabilidades de severidad alta o crítica.

---

## 4. Pipeline de CI/CD en GitHub Actions (IE4)
El archivo `.github/workflows/ci.yml` automatiza el ciclo de vida del software ante eventos de `push` y `pull_request` sobre las ramas `develop` y `main`:
1. **Pruebas y Cobertura:** Compila el microservicio, ejecuta los tests unitarios y publica el reporte de JaCoCo como artefacto.
2. **Escaneo de Seguridad Snyk:** Audita las librerías de dependencias.
3. **Análisis SonarCloud:** Procesa las métricas de código y valida el Quality Gate.
4. **Construcción de Imagen Docker:** Construye la imagen para validar la reproducibilidad del entorno.

---

## 5. Orquestación con Docker Compose (IE5)
En el archivo `docker-compose.yml` se orquestan los servicios necesarios para el microservicio:
* **Servicio de Base de Datos:** MariaDB 11.4 con persistencia en el volumen `mariadb_data` y mecanismo de comprobación de salud (`healthcheck`).
* **Microservicio Java:** Configurado para esperar el estado saludable de la base de datos (`condition: service_healthy`).
* **Gobernanza y Recursos:** Se definieron límites de memoria y CPU para evitar saturación de recursos en entornos productivos o cloud simulados.

---

## 6. Declaración de Uso de Inteligencia Artificial
Se utilizaron herramientas de Inteligencia Artificial exclusivamente como soporte en la resolución de sintaxis, depuración de workflows y estructuración de la documentación técnica. El diseño, análisis y validación de las pruebas y configuraciones fueron asumidos por el estudiante.

---

## 7. Reflexión Individual Obligatoria

### Kevis Howard González
En esta segunda etapa consolidé la automatización integral de un pipeline de CI/CD para un microservicio Spring Boot. Aprendí a configurar y depurar la integración entre JaCoCo y SonarCloud, entendiendo cómo resolver fallos en el Quality Gate causados por reglas de confiabilidad (como `java:S5863`) y cómo aislar pruebas con Mockito para evitar dependencias con la base de datos en los runners de GitHub Actions. Asimismo, comprendí la relevancia de la contenerización multi-stage y la orquestación con Docker Compose, garantizando que el aplicativo sea reproducible, seguro y trazable desde el repositorio hasta su despliegue operativo.