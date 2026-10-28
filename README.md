
# Microservicio de Estadísticas — Ingeniería DevOps (DOY0101)

Repositorio para la **Evaluación Parcial N°1** de la asignatura Ingeniería DevOps en Duoc UC. Implementa control de versiones bajo el modelo **GitFlow**, documentación técnica de buenas prácticas y automatización mediante **GitHub Actions**.

---

## 1. Integrantes
* **Estudiante:** Kevis Howard González
* **Modalidad:** Individual
* **Asignatura:** DOY0101 - Ingeniería DevOps
* **Institución:** Duoc UC

---

## 2. Modelos de Ramificación y Justificación (IE1)

| Modelo de Ramificación | Características principales | Ventajas en entornos Cloud |
| :--- | :--- | :--- |
| **GitFlow** | Utiliza ramas troncales estables (`main`, `develop`) y ramas auxiliares de ciclo corto (`feature/*`, `hotfix/*`). | Ideal para despliegues controlados; aísla el desarrollo del código estable en producción. |
| **Trunk-Based Development (TBD)** | Todos los desarrolladores integran directamente en una única rama (`main`) con ramas muy cortas. | Agiliza la entrega continua (CI/CD), pero requiere alta cobertura de pruebas automatizadas. |
| **GitHub Flow** | Modelo simplificado basado en `main` y ramas feature que se despliegan tras revisión. | Muy ágil para proyectos pequeños o despliegues continuos rápidos. |

### Justificación de GitFlow:
Se eligió **GitFlow** porque permite aislar las nuevas características en la rama `develop` sin comprometer la estabilidad del entorno productivo (`main`). Además, ofrece un mecanismo formal mediante ramas `hotfix` para resolver incidentes urgentes directamente en producción sin arrastrar código incompleto desde desarrollo.

---

## 3. Guía de Buenas Prácticas del Repositorio (IE5)

* **Naming de ramas:**
  * `main`: Código probado y estable en producción.
  * `develop`: Integración de las funcionalidades terminadas.
  * `feature/<nombre>`: Nuevas funcionalidades (ej: `feature/actualizar-documentacion`).
  * `hotfix/<nombre>`: Parches críticos para producción (ej: `hotfix/ajuste-puerto`).
* **Mensajes de commit (Conventional Commits):**
  * `feat:` Nueva característica.
  * `fix:` Corrección de fallos.
  * `docs:` Modificaciones de documentación.
  * `ci:` Ajustes en pipelines y flujos automatizados.
* **Flujos de Merge y Revisión:**
  * No se permiten commits directos sobre `main` ni sobre `develop`. Todo cambio ingresa mediante **Pull Requests (PR)** aprobados y validados.

---

## 4. Pipeline de CI con GitHub Actions (IE3 / IE4)
El flujo `.github/workflows/ci.yml` se dispara ante:
* Eventos `push` a la rama `develop`.
* Eventos `pull_request` dirigidos a la rama `main`.

Prepara un contenedor Ubuntu con JDK 17 (Temurin) y ejecuta la compilación Maven (`mvn clean compile -DskipTests`), validando la integridad del microservicio de forma automatizada.

---

## 5. Declaración de Uso Ético de IA
Se utilizó asistencia de inteligencia artificial para apoyar la diagramación y el formato Markdown del documento. El diseño técnico y la ejecución de comandos fueron validados y realizados por el estudiante.

---

## 6. Reflexión Individual Obligatoria

### Reflexión: Kevis Howard González
Durante este encargo aprendí a estructurar un ciclo de desarrollo colaborativo aplicando GitFlow, comprendiendo el beneficio de proteger la rama `main` y mantener la integración en `develop`. Además, configuré el pipeline de GitHub Actions para verificar la compilación del microservicio antes de cada integración. Mi aporte consistió en preparar el código base, organizar las ramas, crear y fusionar los Pull Requests correspondientes y redactar la documentación técnica requerida.

