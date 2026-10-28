# Microservicio de Estadísticas — Ingeniería DevOps (DOY0101)

Repositorio oficial para la **Evaluación Parcial N°1** de la asignatura **Ingeniería DevOps** (Duoc UC). Implementa control de versiones formal bajo el modelo **GitFlow**, simulación colaborativa con trazabilidad completa, pipeline de CI con **GitHub Actions** y guía de gobernanza y buenas prácticas.

---

## 👥 Datos del Proyecto
* **Estudiante:** Kevis Howard González
* **Modalidad:** Individual
* **Asignatura:** DOY0101 - Ingeniería DevOps
* **Microservicio:** Estadísticas (Java 17 / Spring Boot / Apache Maven)
* **Repositorio:** `https://github.com/kevishowardgonzalez/EstadisticasMs-Devops`

---

## 1. Análisis y Justificación de Modelos de Ramificación en Cloud (IE1)

| Criterio Técnico | GitFlow | Trunk-Based Development (TBD) | GitHub Flow |
| :--- | :--- | :--- | :--- |
| **Estructura de ramas** | Múltiples ramas duraderas (`main`, `develop`) y ramas auxiliares de ciclo corto (`feature/*`, `hotfix/*`). | Rama troncal única (`main`) con ramas secundarias efímeras (horas de duración). | Rama principal `main` y ramas de funcionalidad simples que se integran directo tras revisión. |
| **Ciclo de integración** | Planificado por versiones o releases agrupados tras estabilización en `develop`. | Continuo; commits e integraciones constantes directo a la rama troncal. | Continuo y orientado a despliegues directos a producción tras aprobación de PR. |
| **Dependencia de CI/CD** | Media-Alta; valida cada etapa en etapas clave de ramas. | Crítica y obligatoria; si falla una prueba, se bloquea el trabajo de todo el equipo. | Alta; cada PR debe validarse automáticamente antes de liberarse. |
| **Manejo de incidentes** | Ramas aisladas `hotfix/*` directas desde `main` que no tocan trabajo en desarrollo. | Parches aplicados directamente sobre la rama principal (*Fix-Forward*). | Corrección rápida en una rama feature directa y nuevo despliegue. |
| **Aplicabilidad en Cloud** | Ideal para arquitecturas empresariales con ambientes segregados (Dev, Staging y Prod). | Óptimo para microservicios maduros con entrega continua y Feature Flags. | Ideal para aplicaciones web simples de entrega ágil. |

### Justificación Técnica de la Elección: GitFlow
Se eligió **GitFlow** debido a que responde a los requerimientos de gobernanza y estabilidad en entornos de nube:
1. **Segregación estricta de entornos:** La rama `main` refleja fielmente el entorno de producción (código estable), mientras que `develop` actúa como integrador continuo. Esto evita que código incompleto impacte el servicio productivo.
2. **Mitigación de incidentes con Hotfix:** Permite aislar soluciones urgentes (`hotfix/*`) directamente en producción sin arrastrar características intermedias de `develop`.
3. **Control y trazabilidad:** Toda integración pasa por Pull Requests obligatorios con validación automatizada.

---

## 2. Simulación Colaborativa y Trazabilidad del Código (IE2)

Se ejecutó el flujo colaborativo aplicando comandos Git (`init`, `checkout`, `commit`, `push`, `pull`, `merge`) con control estricto de trazabilidad mediante Pull Requests:

```text
               (hotfix/ajuste-puerto-produccion)
                /-----------------------------\
               /                               \ PR #3 [Merged]
main ----------------------------------------------------------> (Producción)
       \                                       ^
        \ (Creación develop)                  / (Sincronización final)
develop -------------------------------------------------------> (Integración)
          \                     ^  \                     ^
           \ PR #1 [Merged]    /    \ PR #2 [Merged]    /
    (feature/actualizar-doc)      (feature/mejora-servicio)