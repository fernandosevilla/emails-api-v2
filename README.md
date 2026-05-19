# Emails API v2

API REST de envío de emails desarrollada con **Spring Boot**, **PostgreSQL** y **arquitectura hexagonal**.

El objetivo del proyecto es construir una base sólida, mantenible y cercana a un backend real de producción para gestionar envíos de emails, persistir su estado y evolucionar progresivamente hacia funcionalidades más avanzadas como SMTP real, tracking de aperturas y clicks, seguridad con API Keys, empresas, plantillas y múltiples destinatarios.

---

## Objetivo del proyecto

Este proyecto nace como una API de envío de emails, pero también como un ejercicio práctico para profundizar en buenas prácticas de backend moderno.

La idea es construir una API que permita:

- Enviar emails
- Guardar cada envío en base de datos
- Consultar el estado de los emails
- Enviar emails reales mediante SMTP
- Registrar aperturas mediante pixel de tracking
- Registrar clicks en enlaces
- Proteger la API con Spring Security
- Asociar emails a empresas o clientes
- Crear plantillas reutilizables por empresa
- Enviar emails a múltiples destinatarios

---

## Qué me está aportando

Este proyecto me está sirviendo para practicar y demostrar conceptos importantes de desarrollo backend profesional:

- Diseño de APIs REST
- Arquitectura hexagonal
- Separación entre dominio, aplicación e infraestructura
- Uso de puertos y adaptadores
- Persistencia con PostgreSQL y JPA
- Migraciones con Flyway
- Testing por capas
- Validación de requests
- Manejo centralizado de errores
- Uso de Docker para entorno local
- Evolución de una API desde una versión simple hasta una base más cercana a producto real

La intención no es solo que la API funcione, sino que el código sea claro, mantenible y fácil de extender.

---

## Stack técnico

- Java 25
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker
- Docker Compose
- Actuator
- JUnit
- MockMvc

---

## Arquitectura

El proyecto sigue una arquitectura hexagonal, separando el código en tres grandes zonas:

```text
emails
├── domain
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
└── infrastructure
    ├── config
    ├── web
    │   ├── controller
    │   ├── exception
    │   ├── request
    │   └── response
    ├── persistence
    └── mail
```

### `domain`

Contiene el negocio puro de la aplicación.

Aquí viven conceptos como:

- `EmailMessage`
- `EmailAddress`
- `EmailStatus`

Esta capa no depende de Spring, JPA, REST ni PostgreSQL.

### `application`

Contiene los casos de uso y los puertos.

Por ejemplo:

- `SendEmailUseCase`
- `SendEmailService`
- `EmailMessageRepositoryPort`
- `EmailSenderPort`

La aplicación define lo que necesita, pero no sabe cómo se implementa por fuera.

### `infrastructure`

Contiene los adaptadores concretos:

- Controllers REST
- DTOs de request y response
- Exception handlers
- Entidades JPA
- Repositorios Spring Data
- Adaptadores de persistencia
- Adaptadores de envío de emails

---

## Flujo actual de envío

```text
EmailController
    ↓
SendEmailUseCase
    ↓
SendEmailService
    ↓
EmailMessageRepositoryPort
    ↓
JpaEmailMessageRepositoryAdapter
    ↓
SpringDataEmailMessageRepository
    ↓
PostgreSQL

SendEmailService
    ↓
EmailSenderPort
    ↓
FakeEmailSenderAdapter
```

El controller no contiene lógica de negocio. Solo adapta HTTP al caso de uso.

La aplicación no conoce JPA, PostgreSQL ni SMTP. Solo trabaja con puertos.

---

## Endpoint disponible

### Enviar email

```http
POST /api/v1/emails
```

Ejemplo de request:

```json
{
  "recipient": "cliente@cliente.com",
  "subject": "Bienvenido",
  "content": "Hola, este es un email de prueba."
}
```

Ejemplo de response:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000"
}
```

El email se guarda en base de datos con su estado correspondiente.

---

## Persistencia

Los emails se almacenan en PostgreSQL mediante JPA.

La migración inicial crea la tabla `email_messages` con información como:

- ID
- Destinatario
- Asunto
- Contenido
- Estado
- Fecha de creación
- Fecha de envío
- Fecha de fallo
- Motivo de fallo

Flyway se encarga de aplicar las migraciones y JPA valida que el modelo coincida con la base de datos.

---

## Cómo levantar el proyecto

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd emails-api-v2
```

### 2. Levantar los servicios con Docker Compose

```bash
docker compose up -d --build
```

Esto levantará la aplicación y la base de datos PostgreSQL.

### 3. Comprobar el estado de la aplicación

```http
GET /actuator/health
```

Respuesta esperada:

```json
{
  "status": "UP"
}
```

---

## Ejecutar tests

En Linux/macOS:

```bash
./mvnw test
```

En Windows PowerShell:

```powershell
.\mvnw test
```

---

## Roadmap

El roadmap completo del proyecto está documentado en:

[docs/roadmap-emails-api-v2.md](docs/roadmap-emails-api-v2.md)

Resumen de objetivos:

```text
Objetivo 0  — Proyecto base                         Hecho
Objetivo 1  — Estructura de paquetes                 Hecho
Objetivo 2  — Dominio mínimo                         Hecho
Objetivo 3  — Primer caso de uso                     Hecho
Objetivo 4  — Puertos de salida                      Hecho
Objetivo 5  — Adaptadores fake                       Hecho
Objetivo 6  — Adaptador REST                         Hecho
Objetivo 7  — Persistencia real con PostgreSQL/JPA   Hecho
Objetivo 7.5 — README del proyecto                   Hecho
Objetivo 8  — Consultar emails                       Pendiente
Objetivo 9  — Envío real por SMTP                    Pendiente
Objetivo 10 — Tracking de aperturas                  Pendiente
Objetivo 11 — Tracking de clicks                     Pendiente
Objetivo 12 — Spring Security / API Keys             Pendiente
Objetivo 13 — Envío a múltiples destinatarios        Pendiente
Objetivo 14 — Empresas/clientes                      Pendiente
Objetivo 15 — Plantillas de email por empresa        Pendiente
Objetivo 16 — Variables dinámicas en plantillas      Pendiente
Objetivo 17 — Panel o dashboard                      Futuro
```

---

## Próximos pasos

El siguiente objetivo será añadir la consulta de emails por id:

```http
GET /api/v1/emails/{id}
```

Esto permitirá consultar el estado y los datos de un email previamente enviado.

Después se abordará el envío real por SMTP y, más adelante, tracking, seguridad, empresas y plantillas.

---

## Nota

Este proyecto está en evolución y se está construyendo de forma incremental, priorizando una arquitectura clara y una base técnica sólida antes de añadir funcionalidades avanzadas.
