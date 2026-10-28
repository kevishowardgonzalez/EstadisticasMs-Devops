# Microservicio Estadísticas — Evaluación Parcial 2 (DOY0101)

Este repositorio contiene la entrega de la **Evaluación Parcial N°2** de la asignatura **Ingeniería DevOps** (Duoc UC). Se automatiza el ciclo de vida del microservicio incorporando contenerización multi-stage, orquestación con dependencias saludables, métricas de cobertura de código con JaCoCo y análisis de seguridad con Snyk en GitHub Actions.

---

## 👥 Datos del Proyecto
* **Estudiante:** Kevis Howard González
* **Modalidad:** Individual
* **Asignatura:** DOY0101 - Ingeniería DevOps
* **Microservicio:** Estadísticas (Java 21 / Spring Boot 3.2.5 / Maven / MariaDB)
* **Repositorio:** `https://github.com/kevishowardgonzalez/EstadisticasMs-Devops`

---

## 1. Contenerización Multi-Stage (IE1)
Se implementó un archivo `Dockerfile` utilizando la técnica de **Multi-Stage Build** para minimizar el tamaño de la imagen final y garantizar que herramientas de compilación no pasen a producción:
* **Stage 1 (build):** Utiliza `eclipse-temurin:21-jdk`, descarga dependencias y compila el binario ejecutable (`app.jar`).
* **Stage 2 (runtime):** Utiliza `eclipse-temurin:21-jre`, copiando exclusivamente el archivo JAR empaquetado, reduciendo la superficie de ataque y el peso de la imagen.

---

## 2. Pruebas Automatizadas y Cobertura con JaCoCo (IE2)
En el archivo `pom.xml` se integró el plugin `jacoco-maven-plugin` (versión 0.8.12):
* **Fase de preparación (`prepare-agent`):** Habilita el agente para monitorear las líneas de código ejecutadas durante los tests unitarios.
* **Fase de reporte (`report`):** Genera el informe detallado en `target/site/jacoco/index.html`.
* **Automatización en Pipeline:** El job `test-and-coverage` ejecuta `mvn clean test jacoco:report` y publica el reporte como artefacto descargable en GitHub Actions.

---

## 3. Seguridad y Límites de Recursos (IE3)
* **Escaneo de Vulnerabilidades (SCA):** Se implementó el análisis de dependencias mediante **Snyk CLI** en el pipeline, configurado con severidad alta (`--severity-threshold=high`) para detectar riesgos y emitir un reporte `snyk-report.json`.
* **Políticas de Gobernanza:** Bloqueo de integraciones no seguras antes de la etapa de empaquetado y construcción.
* **Límites de Recursos:** En el entorno de ejecución se aplican límites de 1 núcleo de CPU y 1 GB de memoria RAM (con reserva mínima de 0.5 CPU y 512 MB de RAM) para prevenir agotamiento de recursos (*Noisy Neighbor*).

---

## 4. Despliegue y Trazabilidad en CI/CD (IE4)
El pipeline automatizado en `.github/workflows/ci.yml` asegura un ciclo continuo compuesto por 3 etapas secuenciales:
1. `Pruebas y Cobertura JaCoCo`: Validación de calidad del código fuente.
2. `Escaneo de Seguridad Snyk`: Análisis estático de librerías de terceros.
3. `Construcción de Contenedor Docker`: Empaquetado de la imagen final solo si las pruebas y la seguridad resultan exitosas.

---

## 5. Orquestación con Docker Compose (IE5)
Se definió el archivo `docker-compose.yml` para gestionar la arquitectura distribuida del microservicio:
* **Servicio de Base de Datos (`mariadb`):** Configurado con credenciales de entorno y verificación activa (`healthcheck`) mediante comando nativo de ping.
* **Servicio Backend (`estadisticas-ms`):** Construcción automática desde el `Dockerfile` local, vinculación al puerto `8082` y dependencia estricta (`depends_on: mariadb: condition: service_healthy`) que garantiza que el backend inicie únicamente cuando MariaDB acepte conexiones.

---

## 6. Declaración de Uso de IA
Se utilizó asistencia de Inteligencia Artificial para el formato Markdown y la verificación sintáctica de pipelines YAML. Las decisiones arquitectónicas, configuraciones de servicios y validaciones fueron realizadas por el estudiante.

---

## 7. Reflexión Individual Obligatoria

### Reflexión: Kevis Howard González
Durante el desarrollo de la Evaluación Parcial 2 consolidé habilidades técnicas avanzadas de DevOps.  Cree nuevamente la estructura aprendida
en la E1, para tenr la base para continuar con la E2.
Comprendí el valor práctico de las construcciones multi-stage de Docker para producir imágenes ligeras 
y seguras sin dependencias innecesarias de compilación. La incorporación de JaCoCo en el pipeline me demostró 
cómo auditar objetivamente la calidad del software a través de reportes de cobertura, mientras que la integración de Snyk y 
límites de memoria en Docker Compose reforzó la importancia del enfoque DevSecOps y la gobernanza de recursos antes de desplegar 
en entornos en la nube. Mi contribución consistió en la redacción del Dockerfile, la configuración del archivo docker-compose con 
dependencias saludables, la adaptación del pom.xml y la estructuración del pipeline automatizado en GitHub Actions.