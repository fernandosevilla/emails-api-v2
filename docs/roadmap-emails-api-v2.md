# Roadmap de la v2 — Emails API

## Visión general

Este proyecto es una API de envío de emails desarrollada con **Spring Boot** siguiendo una arquitectura **hexagonal**.

La idea es construir una base sólida, profesional y mantenible para una API que permita:

- Enviar emails.
- Persistir los envíos.
- Consultar el estado de los emails.
- Enviar emails reales por SMTP.
- Añadir tracking de aperturas y clicks.
- Proteger la API con Spring Security.
- Evolucionar hacia un sistema con empresas, plantillas y múltiples destinatarios.

El objetivo no es solo crear una API funcional, sino usar el proyecto para aprender y demostrar buenas prácticas de backend moderno:

- Arquitectura hexagonal.
- Separación entre dominio, aplicación e infraestructura.
- Testing.
- Persistencia con PostgreSQL.
- Flyway.
- JPA.
- Docker.
- Seguridad.
- Diseño de APIs REST.
- Código limpio y mantenible.

---

## Estado actual resumido

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

# Objetivo 0 — Proyecto base

## Objetivo

Crear la base técnica del proyecto.

## Incluye

- Spring Boot.
- Maven.
- Dockerfile.
- Docker Compose.
- PostgreSQL.
- `application.yaml`.
- Actuator.
- Flyway.
- JPA en modo `validate`.

## Capa afectada

```text
raíz del proyecto
configuración
src/main/resources
```

## Estado

```text
Hecho
```

---

# Objetivo 1 — Estructura de paquetes

## Objetivo

Definir una estructura inicial siguiendo arquitectura hexagonal.

## Estructura objetivo

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
        ├── controller
        ├── exception
        ├── request
        └── response
    ├── persistence
    └── mail
```

## Significado de cada capa

### `domain`

Contiene el negocio puro:

- Entidades.
- Value Objects.
- Estados.
- Reglas de negocio.

No debe depender de Spring, JPA, REST ni base de datos.

### `application`

Contiene los casos de uso.

Ejemplos:

- Enviar email.
- Consultar email.
- Registrar apertura.
- Registrar click.

### `application/port/in`

Define las entradas a la aplicación.

Ejemplos:

- `SendEmailUseCase`.
- `GetEmailUseCase`.

### `application/port/out`

Define lo que la aplicación necesita del exterior.

Ejemplos:

- Guardar emails.
- Enviar emails.
- Obtener hora actual.
- Generar identificadores.

### `infrastructure/web`

Adaptadores de entrada HTTP:

- Controllers.
- Requests.
- Responses.
- Exception handlers.

### `infrastructure/persistence`

Adaptadores de salida a base de datos:

- Entidades JPA.
- Repositorios Spring Data.
- Mappers.
- Adaptadores.

### `infrastructure/mail`

Adaptadores de salida para envío de emails:

- Fake sender.
- SMTP.
- Proveedores externos.

## Estado

```text
Hecho
```

---

# Objetivo 2 — Dominio mínimo

## Objetivo

Crear el modelo de dominio principal del sistema.

## Pregunta clave

¿Qué es un email para nuestro sistema?

## Modelo inicial

Un email tiene:

- Id.
- Destinatario.
- Asunto.
- Contenido.
- Estado.
- Fecha de creación.
- Fecha de envío.
- Fecha de fallo.
- Motivo de fallo.

## Estados iniciales

```text
PENDING
SENT
FAILED
```

## Clases principales

- `EmailMessage`.
- `EmailStatus`.
- `EmailAddress`.

## Reglas importantes

El dominio no debe usar anotaciones como:

- `@Entity`.
- `@Table`.
- `@Service`.
- `@RestController`.
- `@Repository`.

El dominio debe tener comportamiento propio, por ejemplo:

- Marcar un email como enviado.
- Marcar un email como fallido.
- Controlar cambios de estado.
- Evitar setters públicos innecesarios.

## Estado

```text
Hecho
```

---

# Objetivo 3 — Primer caso de uso

## Objetivo

Crear el primer caso de uso de la aplicación: enviar email.

## Flujo inicial

```text
1. Recibir los datos necesarios.
2. Crear un EmailMessage.
3. Guardarlo como PENDING.
4. Intentar enviarlo.
5. Si se envía correctamente, marcarlo como SENT.
6. Si falla, marcarlo como FAILED.
7. Guardar el resultado final.
8. Devolver el id.
```

## Clases principales

- `SendEmailUseCase`.
- `SendEmailService`.
- `SendEmailCommand`.

## Idea importante

Enviar un email no significa todavía usar SMTP.

En este punto solo definimos el flujo de aplicación.

## Estado

```text
Hecho
```

---

# Objetivo 4 — Puertos de salida

## Objetivo

Definir las interfaces que necesita la aplicación para hablar con el exterior.

## Puertos iniciales

- `EmailMessageRepositoryPort`.
- `EmailSenderPort`.

## Idea importante

La aplicación dice:

```text
Necesito guardar emails, pero me da igual cómo.
```

Y también:

```text
Necesito enviar emails, pero me da igual si es SMTP, Mailgun, Resend, Amazon SES o un fake.
```

## Estado

```text
Hecho
```

---

# Objetivo 5 — Adaptadores fake

## Objetivo

Crear adaptadores temporales para probar el flujo sin PostgreSQL ni SMTP real.

## Adaptadores

- `FakeEmailSenderAdapter`.
- `InMemoryEmailMessageRepositoryAdapter`.

## Configuración

- `EmailBeanConfiguration`.

## Flujo

```text
SendEmailService
    ↓
EmailMessageRepositoryPort
    ↓
InMemoryEmailMessageRepositoryAdapter

SendEmailService
    ↓
EmailSenderPort
    ↓
FakeEmailSenderAdapter
```

## Idea importante

Aunque sean fake o in-memory, siguen siendo infraestructura porque son implementaciones concretas de los puertos.

## Estado

```text
Hecho
```

---

# Objetivo 6 — Adaptador REST

## Objetivo

Exponer el caso de uso mediante HTTP.

## Endpoint

```http
POST /api/v1/emails
```

## Clases principales

- `EmailController`.
- `SendEmailRequest`.
- `SendEmailResponse`.
- `WebExceptionHandler`.
- `ValidationErrorResponse`.
- `FieldValidationErrorResponse`.

## Responsabilidades del controller

El controller solo debe:

```text
1. Recibir JSON.
2. Validar datos de entrada.
3. Convertir request a command.
4. Llamar al caso de uso.
5. Devolver response.
```

El controller no debe:

- Mandar emails.
- Guardar emails.
- Decidir estados.
- Contener lógica de negocio.

## Estado

```text
Hecho
```

---

# Objetivo 7 — Persistencia real con PostgreSQL/JPA

## Objetivo

Sustituir la persistencia en memoria por una implementación real con PostgreSQL, JPA y Flyway.

## Endpoint afectado

```http
POST /api/v1/emails
```

El endpoint debe seguir funcionando igual desde fuera.

La diferencia es que ahora los emails se guardan realmente en PostgreSQL.

## Clases y archivos principales

- `V1__create_email_messages_table.sql`.
- `EmailMessageJpaEntity`.
- `SpringDataEmailMessageRepository`.
- `JpaEmailMessageRepositoryAdapter`.
- `EmailMessagePersistenceMapper`.

## Arquitectura esperada

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
```

## Idea importante

La capa `application` sigue usando únicamente:

```text
EmailMessageRepositoryPort
```

No debe conocer:

- JPA.
- Spring Data.
- PostgreSQL.
- Entidades con `@Entity`.

## Tests esperados

- Test del mapper de persistencia.
- Test del adaptador JPA.
- Test del flujo verificando que el email queda persistido.
- Ejecución completa de tests con Maven.

## Estado

```text
Hecho
```

---

# Objetivo 7.5 — README del proyecto

## Objetivo

Crear un `README.md` profesional para explicar el proyecto.

## Motivo

Este objetivo se adelanta porque el proyecto ya tiene suficiente base como para enseñarlo de forma seria:

- Base técnica.
- Arquitectura hexagonal.
- Dominio.
- Caso de uso.
- Endpoint REST.
- Persistencia real.

## El README debe explicar

- Qué es el proyecto.
- Para qué sirve.
- Qué problema resuelve.
- Qué está aportando a nivel de aprendizaje.
- Arquitectura utilizada.
- Stack técnico.
- Cómo levantar el proyecto.
- Cómo ejecutar tests.
- Endpoints disponibles.
- Roadmap.
- Estado actual.

## Estado

```text
Hecho
```

---

# Objetivo 8 — Consultar emails

## Objetivo

Añadir un caso de uso para consultar un email por id.

## Endpoint

```http
GET /api/v1/emails/{id}
```

## Clases principales

- `GetEmailUseCase`.
- `GetEmailService`.
- `GetEmailQuery` o `GetEmailCommand`.
- `EmailDetailsResponse`.
- `EmailNotFoundException`.

## Cambios necesarios

En el puerto de repositorio:

```text
findById
```

En infraestructura:

```text
implementación JPA de findById
```

## Idea importante

Este objetivo ayuda a ver cómo una funcionalidad atraviesa todas las capas:

```text
REST
↓
application
↓
port/out
↓
infrastructure/persistence
↓
database
```

## Estado

```text
Pendiente
```

---

# Objetivo 9 — Envío real por SMTP

## Objetivo

Sustituir el adaptador fake de envío por un adaptador real SMTP.

## Clases principales

- `SmtpEmailSenderAdapter`.

## Configuración esperada

- Host SMTP.
- Puerto.
- Usuario.
- Contraseña.
- Remitente.
- TLS/SSL si aplica.

## Idea importante

La aplicación no debe cambiar.

Solo se cambia la implementación de:

```text
EmailSenderPort
```

Esto demuestra una ventaja clara de la arquitectura hexagonal.

## Estado

```text
Pendiente
```

---

# Objetivo 10 — Tracking de aperturas

## Objetivo

Registrar cuándo se abre un email mediante un pixel de tracking.

## Endpoint

```http
GET /t/open/{trackingId}.png
```

## Conceptos nuevos

- `TrackingId`.
- `EmailOpenEvent`.
- `RegisterEmailOpenUseCase`.

## Datos a registrar

- Email asociado.
- Fecha de apertura.
- IP.
- User-Agent.
- TrackingId.

## Idea importante

El endpoint devolverá una imagen transparente de 1x1 píxel.

Cuando el cliente de correo cargue esa imagen, se registrará la apertura.

## Estado

```text
Pendiente
```

---

# Objetivo 11 — Tracking de clicks

## Objetivo

Registrar clicks en enlaces enviados dentro de emails.

## Endpoint

```http
GET /r/click/{trackingId}
```

## Conceptos nuevos

- `EmailClickEvent`.
- `RegisterEmailClickUseCase`.

## Flujo esperado

```text
1. El usuario hace click en un enlace del email.
2. El enlace pasa por nuestra API.
3. Registramos el click.
4. Redirigimos al usuario al enlace original.
```

## Datos a registrar

- Email asociado.
- URL original.
- Fecha del click.
- IP.
- User-Agent.
- TrackingId.

## Estado

```text
Pendiente
```

---

# Objetivo 12 — Spring Security / API Keys

## Objetivo

Proteger la API para que no sea pública.

## Primera versión recomendada

Usar API Keys.

## Ejemplo de uso

```http
X-API-Key: api_key_del_cliente
```

## Incluye

- Spring Security.
- Filtro de API Key.
- Protección de endpoints.
- Tests de seguridad.
- Posible tabla de clientes o empresas.
- API Keys asociadas a empresas/clientes.

## Endpoints protegidos

Principalmente:

```http
POST /api/v1/emails
GET /api/v1/emails/{id}
```

## Endpoints públicos futuros

Los endpoints de tracking podrían necesitar ser públicos:

```http
GET /t/open/{trackingId}.png
GET /r/click/{trackingId}
```

## Evolución futura

Más adelante se podría evolucionar a:

- JWT.
- OAuth2.
- Roles.
- Permisos.
- Panel de administración.

## Estado

```text
Pendiente
```

---

# Objetivo 13 — Envío a múltiples destinatarios

## Objetivo

Permitir enviar un email a varios destinatarios.

## Ejemplo de request

```json
{
  "recipients": [
    "cliente1@email.com",
    "cliente2@email.com"
  ],
  "subject": "Nuevo aviso",
  "content": "Contenido del email"
}
```

## Decisión recomendada

Crear un `EmailMessage` por destinatario.

## Motivo

Cada destinatario puede tener:

- Estado distinto.
- Error distinto.
- Aperturas distintas.
- Clicks distintos.
- Tracking distinto.

## Estado

```text
Pendiente
```

---

# Objetivo 14 — Empresas/clientes

## Objetivo

Añadir el concepto de empresa o cliente que usa la API.

## Conceptos nuevos

- `Company`.
- `ApiKey`.
- Configuración por empresa.
- Emails asociados a empresa.

## Motivo

Esto convierte el proyecto en algo más cercano a un SaaS real.

Cada empresa podría tener:

- Sus propias API Keys.
- Sus propios emails.
- Sus propias plantillas.
- Sus propias estadísticas.
- Su propia configuración de envío.

## Estado

```text
Pendiente
```

---

# Objetivo 15 — Plantillas de email por empresa

## Objetivo

Permitir crear plantillas reutilizables de email.

## Ejemplos de plantillas

- Bienvenida.
- Recuperación de contraseña.
- Newsletter.
- Aviso interno.
- Factura enviada.
- Confirmación de cita.

## Conceptos nuevos

- `EmailTemplate`.
- `TemplateVariable`.
- Plantillas asociadas a empresa.

## Estado

```text
Pendiente
```

---

# Objetivo 16 — Variables dinámicas en plantillas

## Objetivo

Permitir renderizar plantillas con variables dinámicas.

## Ejemplo de plantilla

```text
Hola {{name}},

Tu pedido {{order_number}} ha sido enviado.
```

## Ejemplo de request

```json
{
  "templateId": "welcome",
  "recipient": "cliente@email.com",
  "variables": {
    "name": "Fernando",
    "order_number": "12345"
  }
}
```

## Idea importante

El sistema debe sustituir las variables antes de enviar el email.

## Estado

```text
Pendiente
```

---

# Objetivo 17 — Panel o dashboard

## Objetivo

Crear una interfaz visual para consultar y gestionar el sistema.

## Funcionalidades posibles

- Ver emails enviados.
- Ver emails fallidos.
- Ver aperturas.
- Ver clicks.
- Ver estadísticas.
- Gestionar empresas.
- Gestionar API Keys.
- Gestionar plantillas.
- Consultar actividad reciente.

## Opciones de stack

- Frontend separado con React, Vue o Angular.
- Panel interno con Spring Boot + Thymeleaf.
- Otro proyecto separado consumiendo la API.
- Dashboard externo dentro de otro stack.

## Estado

```text
Futuro
```

---

# Orden recomendado a partir de ahora

```text
1. Crear Objetivo 7.5 — README del proyecto.
2. Crear Objetivo 8 — Consultar emails.
3. Crear Objetivo 9 — Envío real por SMTP.
4. Crear Objetivo 10 — Tracking de aperturas.
5. Crear Objetivo 11 — Tracking de clicks.
6. Crear Objetivo 12 — Spring Security / API Keys.
7. Crear Objetivo 13 — Envío a múltiples destinatarios.
8. Crear Objetivo 14 — Empresas/clientes.
9. Crear Objetivo 15 — Plantillas por empresa.
10. Crear Objetivo 16 — Variables dinámicas.
11. Crear Objetivo 17 — Panel o dashboard.
```
