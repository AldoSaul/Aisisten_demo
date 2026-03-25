# Documento de Producto (PRD)

## Asistente móvil para gestión de leads de Meta y agendamiento de citas

**Versión:** v1 borrador sólido
**Estado:** listo para revisión funcional y uso con Copilot
**Owner:** Mario V
**Fecha:** 21 de marzo de 2026

---

## 1. Resumen ejecutivo

Se propone desarrollar una **aplicación móvil SaaS** para negocios de servicios que reciben leads desde campañas de **Meta** y necesitan convertirlos en **citas concretadas** o derivarlos eficientemente con un prestador de servicio o agente humano.

La aplicación centralizará la recepción de leads provenientes de **WhatsApp, Instagram y Facebook Messenger**, automatizará la conversación inicial, ayudará a clasificar al lead, propondrá horarios disponibles, registrará citas, gestionará confirmaciones, reprogramaciones y cancelaciones, y permitirá realizar handoff a humano cuando el caso lo requiera.

La primera vertical objetivo será **salud ambulatoria de baja complejidad operativa**, priorizando negocios con agenda programable, como consultorios, clínicas de atención no urgente, terapeutas y dentistas.

El valor principal del producto no será únicamente “usar IA”, sino **reducir el tiempo de respuesta, aumentar la conversión de lead a cita, disminuir la carga operativa del equipo y reducir citas perdidas o no confirmadas**.

---

## 2. Visión del producto

Crear un asistente operativo que convierta automáticamente leads entrantes de campañas de Meta en citas confirmadas, mediante mensajería conversacional, agenda inteligente y coordinación entre automatización y personal humano.

---

## 3. Problema que resuelve

Los negocios de servicios que pautan campañas en Meta suelen enfrentar estos problemas:

* Los leads llegan por distintos canales y no se atienden a tiempo.
* La primera respuesta tarda demasiado y se pierde intención de compra.
* El equipo humano dedica demasiado tiempo a conversaciones repetitivas.
* No existe una conexión ordenada entre conversación, disponibilidad y agenda.
* Muchas citas se pierden por falta de seguimiento, confirmación o reprogramación ágil.
* La información del lead queda dispersa entre chats, notas y calendarios.

Esto genera baja conversión, carga operativa alta y mala experiencia para el prospecto.

---

## 4. Objetivo del producto

### Objetivo principal

Convertir leads provenientes de campañas de Meta en citas confirmadas de forma rápida, ordenada y medible.

### Objetivos de negocio

* Incrementar la tasa de conversión de lead a cita agendada.
* Reducir el tiempo de primera respuesta.
* Reducir el trabajo manual del staff administrativo.
* Aumentar la asistencia efectiva a citas mediante confirmaciones y recordatorios.
* Crear una base reutilizable para múltiples verticales de servicios.

### Objetivos operativos

* Unificar leads de múltiples canales de Meta en una sola aplicación.
* Permitir gestión de agenda desde reglas internas del sistema.
* Mantener sincronización con Google Calendar.
* Ejecutar handoff a humano sin perder contexto.

---

## 5. Alcance del MVP

### Incluido en MVP

1. Recepción de leads desde canales de Meta:

   * WhatsApp Business
   * Instagram Messaging
   * Facebook Messenger

2. Bandeja unificada de conversaciones por negocio.

3. Respuesta automática inicial.

4. Clasificación básica del lead:

   * quiere agendar
   * quiere información
   * requiere atención humana
   * no califica o está fuera de cobertura

5. Recolección mínima de datos para agenda:

   * nombre
   * servicio
   * sucursal o modalidad
   * horario preferido
   * teléfono si aplica

6. Motor interno de agenda con reglas.

7. Propuesta de horarios disponibles.

8. Creación de cita.

9. Confirmación de cita.

10. Reprogramación y cancelación simples.

11. Sincronización con Google Calendar.

12. Handoff a humano con contexto.

13. Notificaciones y recordatorios básicos.

14. App móvil para el negocio.

15. Panel mínimo administrativo dentro de la app para configurar:

* servicios
* horarios
* sucursales
* usuarios
* reglas de confirmación

### Fuera de alcance para MVP

* Cotización compleja con múltiples variables o seguros.
* Expediente clínico.
* Procesamiento de pagos.
* Integración con CRM externo complejo.
* Automatizaciones avanzadas multicanal fuera del ecosistema Meta.
* Analítica predictiva avanzada.
* IA autónoma para decisiones críticas de operación.
* Marketplace de prestadores.

---

## 6. Perfil de cliente ideal

### Vertical inicial recomendada

Negocios de salud con agenda programable y flujo relativamente estructurado:

* consultorios dentales
* terapeutas
* clínicas ambulatorias
* especialistas con consulta programada
* centros de bienestar o rehabilitación con citas

### Criterios del cliente ideal

* Invierte en campañas de Meta o planea hacerlo.
* Recibe leads por mensajería.
* Tiene problemas de seguimiento o conversión.
* Necesita una solución simple y móvil.
* No cuenta con un sistema robusto de agenda conversacional.

---

## 7. Propuesta de valor

**Convierte tus leads de Meta en citas confirmadas en minutos, sin perder conversaciones y sin saturar a tu equipo.**

---

## 8. Principios de diseño del producto

1. **Primero móvil**: la operación principal debe poder resolverse desde una app móvil.
2. **Automatización útil, no invasiva**: automatizar lo repetitivo y escalar a humano lo sensible.
3. **Reglas duras para agenda**: la disponibilidad real no depende de la IA.
4. **IA como capa de interpretación**: entender intención, extraer datos, resumir y redactar.
5. **Tiempo de respuesta inmediato**: el lead debe recibir respuesta casi instantánea.
6. **Multi-tenant desde el inicio**: un sistema, múltiples negocios.
7. **Trazabilidad completa**: toda acción debe poder auditarse.
8. **Minimización de datos**: capturar solo lo necesario para operar.

---

## 9. Arquitectura funcional del producto

### Módulos principales

1. **Integración de canales Meta**

   * recepción de mensajes y eventos
   * identificación del canal de origen
   * normalización de mensajes entrantes

2. **Gestor de conversaciones**

   * historial por lead
   * estado actual de conversación
   * clasificación de intención
   * handoff a humano

3. **Gestor de leads**

   * perfil de lead
   * origen de campaña/canal
   * estado comercial
   * datos capturados

4. **Motor de agenda**

   * disponibilidad por negocio
   * disponibilidad por recurso
   * duración por servicio
   * buffers
   * reglas de anticipación
   * reprogramaciones y cancelaciones

5. **Sincronizador de calendario**

   * conexión con Google Calendar
   * alta/actualización de eventos
   * detección de cambios relevantes

6. **Asistente conversacional**

   * respuesta automática inicial
   * interpretación de intención
   * extracción de entidades
   * propuestas de horario
   * mensajes de confirmación y seguimiento

7. **Módulo de configuración del negocio**

   * servicios
   * sucursales
   * recursos
   * usuarios
   * horarios y reglas
   * mensajes base

8. **App móvil operativa**

   * bandeja de conversaciones
   * agenda del día
   * confirmaciones
   * acciones manuales
   * notificaciones

9. **Reportes básicos**

   * leads recibidos
   * tiempo de respuesta
   * citas agendadas
   * confirmadas
   * canceladas
   * derivadas a humano

---

## 10. Decisiones clave de producto

### 10.1 Sistema maestro de agenda

El sistema maestro de agenda será **interno**. Google Calendar funcionará como mecanismo de sincronización y visibilidad externa, no como fuente maestra de reglas operativas.

### 10.2 Rol de la IA

#### La IA sí debe encargarse de:

* detectar intención del usuario
* extraer datos clave del mensaje
* redactar respuestas naturales
* resumir conversaciones
* ayudar a clasificar leads

#### La IA no debe encargarse de:

* decidir disponibilidad final
* saltarse reglas de agenda
* tomar decisiones críticas sensibles sin reglas
* confirmar horarios inexistentes
* fijar políticas operativas del negocio

### 10.3 Estrategia de canales

Aunque el núcleo del producto será compartido, cada canal de Meta se modelará como una integración diferenciada a nivel técnico y operacional.

### 10.4 Estrategia de atención

Todo lead debe recibir una **respuesta automática inmediata**. El sistema podrá continuar automáticamente hasta agendar, informar o derivar.

### 10.5 Handoff a humano

Debe existir una forma clara de escalar a humano cuando:

* el lead lo solicite
* la confianza del sistema sea baja
* el caso sea sensible
* haya ambigüedad operativa
* se detecte una excepción de negocio

---

## 11. Flujos principales de usuario

### Flujo 1: Lead desde campaña de Meta a cita confirmada

1. El lead entra por WhatsApp, Instagram o Messenger.
2. El sistema crea o actualiza el registro del lead.
3. Se envía respuesta automática inicial.
4. El asistente detecta intención de agenda.
5. Solicita datos mínimos faltantes.
6. Consulta disponibilidad interna.
7. Propone horarios.
8. El lead elige uno.
9. El sistema registra la cita.
10. Sincroniza con Google Calendar.
11. Envía confirmación.
12. Programa recordatorio.

### Flujo 2: Lead con duda general

1. El lead consulta información.
2. El asistente responde con base en configuración del negocio.
3. Si detecta intención posterior de agenda, transiciona al flujo de cita.
4. Si no puede resolver, deriva a humano.

### Flujo 3: Handoff a humano

1. El sistema detecta excepción o solicitud explícita.
2. Marca conversación como “requiere atención humana”.
3. Genera resumen del caso.
4. Notifica al usuario interno.
5. El agente humano toma control.

### Flujo 4: Confirmación y reprogramación

1. Antes de la cita se envía recordatorio.
2. El lead confirma, cancela o solicita reprogramar.
3. El sistema actualiza agenda.
4. Sincroniza cambios con el calendario.
5. Notifica al negocio.

---

## 12. Roles de usuario

### 1. Administrador del negocio

* configura la cuenta
* gestiona servicios, horarios, sucursales y recursos
* visualiza métricas
* define mensajes y reglas

### 2. Operador / recepcionista

* atiende conversaciones derivadas
* confirma citas manualmente
* reprograma o cancela
* consulta agenda

### 3. Prestador de servicio

* visualiza citas asignadas
* revisa agenda personal
* recibe derivaciones o notificaciones

### 4. Lead / prospecto

* interactúa por mensajería
* solicita información
* agenda, confirma, reprograma o cancela

---

## 13. Requerimientos funcionales

### 13.1 Gestión de leads

* Crear lead nuevo automáticamente al recibir mensaje entrante.
* Vincular múltiples conversaciones al mismo lead cuando sea posible.
* Registrar canal de origen y timestamp.
* Permitir ver estado del lead.

### 13.2 Conversaciones

* Mostrar conversación por canal.
* Registrar mensajes entrantes y salientes.
* Permitir intervención manual.
* Etiquetar conversaciones por estado.

### 13.3 Agenda

* Configurar duración por servicio.
* Configurar disponibilidad por sucursal y recurso.
* Bloquear horarios no disponibles.
* Permitir agendar, reprogramar y cancelar.
* Evitar doble reserva.

### 13.4 Confirmaciones

* Enviar confirmación automática tras agendar.
* Enviar recordatorios automáticos previos.
* Registrar confirmación del lead.

### 13.5 Handoff

* Permitir que el sistema o el usuario interno cambien a modo humano.
* Mantener contexto de la conversación.
* Notificar al responsable.

### 13.6 Configuración

* Alta de negocio.
* Alta de usuarios internos.
* Configuración de servicios.
* Configuración de horarios.
* Configuración de mensajes iniciales.
* Conexión de canales.
* Conexión de Google Calendar.

### 13.7 Reportes

* Número de leads recibidos.
* Número de citas agendadas.
* Número de citas confirmadas.
* Número de citas canceladas.
* Tiempo promedio de primera respuesta.
* Conversaciones derivadas a humano.

---

## 14. Requerimientos no funcionales

* Arquitectura multi-tenant.
* Seguridad por negocio y por rol.
* Trazabilidad de cambios.
* Soporte para crecimiento por volumen de mensajes.
* Tolerancia a reintentos en webhooks.
* Rendimiento suficiente para respuesta rápida.
* Diseño mobile-first.
* Logs estructurados por conversación y agenda.
* Configuración flexible por negocio.

---

## 15. Modelo conceptual de entidades

### Entidades principales

* Tenant
* User
* ChannelConnection
* Lead
* Contact
* Conversation
* Message
* Service
* Location
* Resource
* AvailabilityRule
* Appointment
* CalendarSync
* Handoff
* Reminder
* AuditLog

### Relaciones base

* Un Tenant tiene muchos Users.
* Un Tenant tiene muchas ChannelConnections.
* Un Lead pertenece a un Tenant.
* Un Lead puede tener muchas Conversations.
* Una Conversation tiene muchos Messages.
* Un Tenant tiene muchos Services, Locations y Resources.
* Un Appointment pertenece a un Lead y a un Resource o Location según configuración.
* Un Appointment puede sincronizarse con uno o más calendarios externos.

---

## 16. Reglas operativas iniciales

1. Ninguna cita puede ser creada fuera de disponibilidad.
2. Ninguna cita puede duplicarse para el mismo recurso y horario.
3. Si faltan datos mínimos, el asistente debe solicitarlos antes de proponer horario.
4. Si el asistente no entiende con confianza suficiente, deriva a humano.
5. Si el lead solicita hablar con una persona, se deriva sin fricción.
6. Toda cita creada debe quedar registrada y trazable.
7. Toda modificación de cita debe sincronizarse con el sistema maestro y posteriormente con el calendario externo.

---

## 17. UX esperada para la app móvil

### Secciones mínimas

* Inicio / resumen del día
* Bandeja de conversaciones
* Agenda del día
* Citas próximas
* Leads nuevos
* Configuración del negocio
* Reportes básicos

### Experiencia esperada

* Acceso rápido a conversaciones urgentes.
* Vista simple de agenda del día.
* Acciones frecuentes en pocos toques.
* Notificaciones útiles y no excesivas.
* Toma de control humano clara.

---

## 18. Stack técnico recomendado para v1

### Frontend móvil

* Flutter

### Frontend web administrativo

* Next.js
* TypeScript
* App Router
* UI component system compartido con design tokens del produ([developers.facebook.com](https://developers.facebook.com/docs/messenger-platform/webhooks/?utm_source=chatgpt.com))dular monolítica en v1

### Base de datos

* PostgreSQL

### Cache / colas / jobs

* Redis

### Integraciones

* Meta messaging webhooks
* Google Calendar API

### Infraestructura sugerida

* despliegue cloud con contenedores
* almacenamiento de secretos seguro
* observabilidad y logs centralizados

## 19. Arquitectura técnica v1

### 19.1 Estilo arquitectónico recomendado

Para v1 se recomienda una **arquitectura modular monolítica** con separación clara por dominios y eventos internos.

Esto significa:

* un solo backend desplegable principal
* módulos internos bien aislados
* contratos internos claros
* procesamiento asíncrono para tareas de integración, sincronización y notificaciones

### Justificación

Este enfoque reduce complejidad operativa al inicio, acelera desarrollo, simplifica depuración y permite evolucionar después hacia servicios separados solo si el volumen o el dominio lo justifican.

### Principio rector

**Monolito modular por ahora, event-driven por dentro, desacoplable después.**

---

### 19.2 Contextos principales del backend

El backend se organizará por módulos de dominio:

1. **Identity & Access**

   * autenticación
   * autorización por rol
   * gestión de usuarios internos
   * pertenencia a tenant

2. **Tenant Management**

   * alta de empresa
   * configuración general
   * parámetros del negocio
   * branding básico

3. **Channel Integrations**

   * conexiones con WhatsApp, Instagram y Messenger
   * validación de webhooks
   * normalización de eventos entrantes
   * envío de mensajes salientes

4. **Lead Management**

   * creación y actualización de leads
   * identidad de contacto
   * asociación con campañas/canales
   * estados del lead

5. **Conversation Management**

   * hilos de conversación
   * historial de mensajes
   * estado conversacional
   * handoff a humano

6. **Scheduling Engine**

   * disponibilidad por sucursal
   * asignación de prestador
   * reglas por servicio
   * slots
   * reprogramación y cancelación

7. **Calendar Sync**

   * sincronización con Google Calendar
   * push notifications
   * reconciliación
   * mapeo appointment-event

8. **Notification & Reminder**

   * confirmaciones
   * recordatorios
   * colas de envío
   * plantillas

9. **AI Orchestration**

   * interpretación de intención
   * extracción de entidades
   * resumen de conversación
   * guardrails y fallback

10. **Reporting & Audit**

* métricas básicas
* trazabilidad
* auditoría de cambios

---

### 19.3 Diagrama lógico de alto nivel

```text
[Meta Channels: WhatsApp / Instagram / Messenger]
                    |
                    v
          [Webhook Ingestion Layer]
                    |
                    v
       [Channel Integration Module]
                    |
                    v
      [Conversation + Lead Management]
                    |
        -------------------------------
        |              |              |
        v              v              v
 [AI Orchestration] [Scheduling] [Handoff / Human Ops]
        |              |
        |              v
        |       [Appointment Management]
        |              |
        ---------------|----------------
                       v
              [Calendar Sync Module]
                       |
                       v
              [Google Calendar API]

Internal users operate through:
- [Mobile App]
- [Web Admin Panel]
```

---

### 19.4 Clientes de aplicación

#### App móvil

Responsabilidades:

* bandeja de conversaciones
* agenda del día
* confirmaciones manuales
* handoff
* notificaciones operativas

#### Panel web administrativo

Responsabilidades:

* onboarding del negocio
* configuración de servicios
* configuración de sucursales y prestadores
* horarios y reglas
* conexión de canales
* conexión de Google Calendar
* administración de usuarios
* revisión de métricas y auditoría básica

---

### 19.5 Arquitectura del backend por capas

Se recomienda seguir esta estructura por módulo:

1. **API Layer**

   * endpoints REST
   * webhooks
   * validación de entrada

2. **Application Layer**

   * casos de uso
   * orquestación de reglas
   * control transaccional

3. **Domain Layer**

   * entidades
   * value objects
   * reglas de negocio
   * políticas de agenda

4. **Infrastructure Layer**

   * ORM
   * repositorios
   * clientes externos
   * colas
   * caché

---

### 19.6 Integraciones externas

#### Meta

Meta soporta recepción de eventos en tiempo real mediante webhooks para sus plataformas de mensajería, y WhatsApp Cloud API permite interacción programática dentro del ecosistema Business Messaging. ([developers.facebook.com](https://developers.facebook.com/docs/messenger-platform/webhooks/?utm_source=chatgpt.com))

##### Diseño recomendado

* endpoint de webhook por proveedor lógico
* verificación de firma y autenticidad
* adaptadores por canal
* normalización a un evento interno común

##### Evento interno sugerido

```json
{
  "event_type": "message.received",
  "channel": "whatsapp|instagram|messenger",
  "tenant_id": "uuid",
  "external_account_id": "string",
  "external_contact_id": "string",
  "conversation_external_id": "string",
  "message_external_id": "string",
  "timestamp": "iso8601",
  "payload": {
    "text": "...",
    "attachments": [],
    "metadata": {}
  }
}
```

#### Google Calendar

Google Calendar expone operaciones para crear eventos y push notifications para observar cambios usando canales de notificación y métodos `watch`, incluyendo `events.watch`. ([developers.google.com](https://developers.google.com/workspace/calendar/api/guides/push?utm_source=chatgpt.com))

##### Diseño recomendado

* un adaptador de calendario por tenant o conexión
* persistir mapping entre `appointment_id` y `google_event_id`
* soportar sincronización saliente e inbound sync
* job de reconciliación periódica ante fallos o expiración de watches

---

### 19.7 Estrategia de mensajería y eventos internos

Aunque el backend sea monolítico, debe usar eventos internos para desacoplar procesos.

#### Eventos internos sugeridos

* `lead.created`
* `lead.updated`
* `conversation.created`
* `message.received`
* `message.sent`
* `intent.detected`
* `appointment.requested`
* `appointment.created`
* `appointment.confirmed`
* `appointment.cancelled`
* `appointment.rescheduled`
* `handoff.requested`
* `calendar.sync.requested`
* `calendar.event.updated`
* `reminder.scheduled`
* `reminder.sent`

#### Objetivo

* reducir acoplamiento entre módulos
* facilitar retries
* mejorar trazabilidad
* preparar futura separación de servicios

---

### 19.8 Modelo de dominio técnico inicial

#### Entidades núcleo

##### Tenant

Representa la empresa cliente.

Campos sugeridos:

* id
* legal_name
* display_name
* timezone
* status
* created_at
* updated_at

##### User

Representa usuario interno del tenant.

Campos sugeridos:

* id
* tenant_id
* email
* full_name
* role
* status
* last_login_at

##### ChannelConnection

Representa una conexión a canal Meta.

Campos sugeridos:

* id
* tenant_id
* channel_type
* external_business_id
* external_account_id
* config_json
* status
* verified_at

##### Lead

Representa un prospecto.

Campos sugeridos:

* id
* tenant_id
* primary_contact_id
* source_channel
* source_campaign_ref
* status
* stage
* assigned_user_id nullable
* created_at

##### Contact

Representa identidad del contacto.

Campos sugeridos:

* id
* tenant_id
* full_name
* phone_e164
* external_profiles_json
* consent_flags_json

##### Conversation

Representa el hilo por canal/contacto.

Campos sugeridos:

* id
* tenant_id
* lead_id
* channel_connection_id
* channel_type
* external_thread_id
* status
* handoff_status
* last_message_at

##### Message

Representa mensaje individual.

Campos sugeridos:

* id
* tenant_id
* conversation_id
* direction
* message_type
* external_message_id
* payload_json
* sent_at
* delivered_at nullable
* read_at nullable

##### Service

Representa un servicio agendable.

Campos sugeridos:

* id
* tenant_id
* name
* duration_minutes
* buffer_before_minutes
* buffer_after_minutes
* requires_provider
* status

##### Location

Representa sucursal.

Campos sugeridos:

* id
* tenant_id
* name
* address_json
* timezone
* status

##### Provider

Representa prestador o profesional.

Campos sugeridos:

* id
* tenant_id
* full_name
* specialty nullable
* status

##### ProviderLocation

Tabla relacional para asignación de prestador a sucursal.

Campos sugeridos:

* provider_id
* location_id
* status

##### AvailabilityRule

Define disponibilidad operativa.

Campos sugeridos:

* id
* tenant_id
* location_id
* provider_id nullable
* service_id nullable
* weekday
* start_time_local
* end_time_local
* slot_granularity_minutes
* valid_from nullable
* valid_until nullable

##### Appointment

Representa cita.

Campos sugeridos:

* id
* tenant_id
* lead_id
* contact_id
* service_id
* location_id
* provider_id nullable
* status
* starts_at
* ends_at
* source_channel
* notes_json
* created_by_type
* created_by_user_id nullable

##### CalendarSync

Mapeo con calendario externo.

Campos sugeridos:

* id
* tenant_id
* appointment_id
* provider_id nullable
* location_id nullable
* integration_type
* external_calendar_id
* external_event_id
* sync_status
* last_synced_at

##### Reminder

Representa confirmación o recordatorio.

Campos sugeridos:

* id
* tenant_id
* appointment_id
* reminder_type
* scheduled_for
* sent_at nullable
* status

##### Handoff

Representa transferencia a humano.

Campos sugeridos:

* id
* tenant_id
* conversation_id
* reason
* assigned_user_id nullable
* status
* created_at

##### AuditLog

Registro de auditoría.

Campos sugeridos:

* id
* tenant_id
* actor_type
* actor_id nullable
* entity_type
* entity_id
* action
* diff_json
* created_at

---

### 19.9 Reglas de agenda técnicas

1. Toda cita debe tener `tenant_id`, `service_id`, `location_id`, `contact_id`, `starts_at`, `ends_at`.
2. `provider_id` será opcional en el dominio, pero debe estar soportado desde v1.
3. No se permite solapamiento para una misma combinación de recurso reservado.
4. El motor debe validar duración por servicio y buffers.
5. La disponibilidad se calcula desde reglas persistidas, no desde IA.
6. Debe existir soporte para citas creadas por automatización o por humano.

#### Estrategia práctica de bloqueo

Para v1, la restricción de conflicto puede operar así:

* si la cita tiene `provider_id`, validar contra agenda del prestador
* si la cita no tiene `provider_id`, validar contra agenda por sucursal o capacidad definida

Esto deja abierta una futura evolución hacia modelo explícito de capacidad o recurso.

---

### 19.10 Estrategia de IA en arquitectura

La IA debe vivir detrás de un módulo de orquestación controlado.

#### Pipeline sugerido

1. llega mensaje entrante
2. se normaliza
3. se evalúa si aplica procesamiento automático
4. se ejecuta clasificación de intención
5. se extraen entidades
6. se valida confianza
7. si la confianza es suficiente, se ejecuta caso de uso
8. si no, se deriva a humano o se pide aclaración controlada

#### Entidades objetivo para extracción

* nombre
* servicio
* sucursal
* teléfono
* horario preferido
* confirmación
* cancelación
* reprogramación
* solicitud de humano

#### Guardrails obligatorios

* jamás crear cita sin validación del motor de agenda
* jamás comprometer cotización automática
* jamás responder casos fuera de política sin fallback
* toda salida importante debe ser auditable

---

### 19.11 Estrategia de persistencia

#### Base transaccional

PostgreSQL será la fuente de verdad del dominio.

#### Redis

Usos sugeridos:

* colas ligeras
* rate limiting interno
* locks transitorios
* cache de slots calculados

#### Archivos y adjuntos

En v1 se recomienda almacenar referencias a object storage, no blobs grandes en base de datos.

---

### 19.12 API pública interna para clientes

#### Recomendación

REST para v1.

#### Motivo

* menor complejidad inicial
* más fácil de consumir desde móvil y panel web
* compatible con documentación clara y generación de SDKs si después se requiere

#### Agrupación sugerida de endpoints

* `/auth/*`
* `/tenants/*`
* `/users/*`
* `/channels/*`
* `/leads/*`
* `/conversations/*`
* `/messages/*`
* `/services/*`
* `/locations/*`
* `/providers/*`
* `/availability/*`
* `/appointments/*`
* `/calendar/*`
* `/reports/*`
* `/webhooks/meta/*`
* `/webhooks/google-calendar/*`

---

### 19.13 Seguridad y control de acceso

#### Requisitos base

* autenticación segura por usuario
* autorización por tenant y rol
* separación estricta de datos entre tenants
* cifrado en tránsito
* secretos fuera del código
* auditoría de cambios críticos

#### Roles iniciales sugeridos

* owner_admin
* manager
* operator
* provider

#### Acciones sensibles que requieren auditoría

* creación o cancelación de cita
* cambio de disponibilidad
* conexión o desconexión de integraciones
* reasignación de conversación
* cambio manual de estado de lead

---

### 19.14 Estrategia de jobs y tareas asíncronas

Usar workers para:

* procesar mensajes entrantes
* enviar mensajes salientes
* ejecutar recordatorios
* sincronizar calendarios
* reconciliar inconsistencias
* generar resúmenes o clasificaciones de IA no críticas en línea

#### Principio

Todo proceso externo, lento o reintentable debe salir del request-response principal cuando sea posible.

---

### 19.15 Observabilidad

#### Logs estructurados

Cada operación relevante debe registrar:

* tenant_id
* correlation_id
* module
* entity_type
* entity_id
* outcome

#### Métricas técnicas mínimas

* latencia de webhook a respuesta
* tasa de error por integración
* jobs procesados
* retries
* fallos de sync calendario
* fallos de envío por canal

#### Trazas deseables

* recepción de mensaje
* clasificación IA
* intento de agenda
* creación de cita
* envío de confirmación
* sync con calendario

---

### 19.16 Estrategia de despliegue

#### Ambientes mínimos

* local
* staging
* production

#### Componentes desplegables iniciales

* backend API
* worker
* postgres
* redis
* web admin
* mobile app distribution pipelines

#### Recomendación operativa

Usar CI/CD con validaciones mínimas:

* lint
* tests unitarios
* tests de integración críticos
* migraciones verificadas
* build de clientes

---

### 19.17 Estructura sugerida de repositorios

#### Opción recomendada v1

Monorepo.

##### Estructura sugerida

```text
/apps
  /mobile
  /web-admin
  /backend
/packages
  /shared-types
  /design-tokens
  /api-contracts
  /lint-config
  /testing-utils
/infrastructure
  /docker
  /ci
  /scripts
/docs
```

### Justificación

* mejor coordinación entre agentes
* más fácil compartir contratos
* mejor experiencia para Copilot y revisión cruzada
* más simple para cambios full-stack

---

### 19.18 Estándares de implementación para Copilot y agentes

Todo desarrollo técnico debe seguir estas reglas:

1. No introducir microservicios en v1.
2. No acoplar lógica de negocio al framework UI.
3. No mezclar reglas de agenda con prompts de IA.
4. Todo caso de uso importante debe tener tests.
5. Todo módulo debe exponer contratos explícitos.
6. Toda integración externa debe estar detrás de adaptadores.
7. Toda entidad de dominio crítica debe tener auditoría.
8. Todo flujo relevante debe contemplar retry o manejo de error.
9. Toda historia implementada debe compilar, pasar lint y pasar los tests relevantes.
10. Todo agente debe dejar evidencia verificable de qué cambió, qué probó y qué quedó pendiente.

---

### 19.19 Decisiones técnicas cerradas para backlog

* arquitectura modular monolítica
* app móvil con Flutter
* panel web con Next.js
* backend con FastAPI
* PostgreSQL como fuente de verdad
* Redis para jobs, locks y cache ligera
* REST para APIs internas
* webhooks para Meta
* Google Calendar con sync + watch
* monorepo para coordinación de desarrollo
* motor de agenda interno
* IA aislada en módulo controlado

## 20. Enfoque de implementación por fases Enfoque de implementación por fases

### Fase 1: MVP utilizable

* onboarding de negocio
* conexión de canal principal
* bandeja de mensajes
* reglas básicas de agenda
* agendamiento simple
* confirmaciones
* handoff
* sincronización con Google Calendar

### Fase 2: robustecimiento

* más canales
* métricas más completas
* plantillas configurables
* score de prioridad de lead
* mejor administración de sucursales y recursos

### Fase 3: expansión

* nuevas verticales
* panel web complementario
* integraciones externas adicionales
* automatizaciones más avanzadas

---

## 20. Métricas clave de éxito

### Métricas de conversión

* lead a conversación efectiva
* conversación a cita agendada
* cita agendada a cita confirmada
* cita confirmada a cita asistida

### Métricas operativas

* tiempo de primera respuesta
* tiempo promedio para concretar cita
* porcentaje de handoff a humano
* tasa de reprogramación
* tasa de cancelación

### Métricas de valor para el cliente

* reducción de carga operativa
* aumento de citas concretadas
* disminución de leads perdidos

---

## 21. Riesgos principales

1. Complejidad variable entre verticales.
2. Dependencia de reglas de negocio mal configuradas.
3. Riesgo de sobrediseñar IA donde bastan reglas simples.
4. Integraciones de mensajería sujetas a políticas y cambios externos.
5. Fricción si la app móvil intenta resolver demasiada administración compleja.
6. Riesgo legal y reputacional al manejar datos sensibles en salud.

---

## 22. Supuestos iniciales

* El canal con mayor valor inicial será WhatsApp.
* La mayoría de negocios objetivo necesitan agenda más que cotización compleja.
* La respuesta inmediata mejora la conversión significativamente.
* La agenda debe operar por reglas internas, no por interpretación libre.
* El negocio valora tanto ahorro operativo como aumento de citas.

---

## 23. Decisiones cerradas y preguntas aún abiertas

### Decisiones cerradas

1. **Modelo de cita**
   La cita deberá soportar relación con **sucursal y prestador**. La sucursal define el contexto operativo principal y el prestador define la asignación específica cuando aplique. Esto permite operar negocios donde la disponibilidad depende primero del lugar y después del profesional o recurso asignado.

2. **Datos mínimos obligatorios antes de ofrecer horario**

   * nombre
   * servicio
   * sucursal
   * teléfono
   * horario preferido

3. **Política de recordatorios por defecto**

   * una sola confirmación por defecto

4. **Cotización en MVP**

   * no habrá cotización automática en el MVP
   * cualquier cotización o caso comercial no estandarizado se deriva a humano

5. **Modelo de monetización inicial**

   * cobro por usuario
   * el usuario estará vinculado al ecosistema empresarial desde donde opera la campaña de Meta y la atención resultante

### Preguntas aún abiertas

1. ¿El panel web administrativo será parte del MVP o solo una fase inmediata posterior?
2. ¿El prestador será obligatorio en toda cita o habrá citas que pertenezcan solo a una sucursal y se asignen después?
3. ¿Qué tan configurable será el horario preferido del lead: rango libre, bloques predefinidos o selección asistida?
4. ¿Qué nivel de visibilidad de campaña de Meta se mostrará dentro del producto en v1?

---

## 24. Prompt base para trabajar con Copilot

Usar este documento como referencia para diseñar e implementar una aplicación SaaS multi-tenant enfocada en convertir leads entrantes de campañas de Meta en citas confirmadas para negocios de servicios, iniciando por salud ambulatoria de baja complejidad. El sistema debe integrar canales de WhatsApp, Instagram y Facebook Messenger mediante webhooks, gestionar conversaciones, clasificar intención, capturar datos mínimos, operar un motor interno de agenda con reglas, sincronizar con Google Calendar y permitir handoff a humano. La IA debe usarse para intención, extracción, resumen y redacción, pero no para tomar decisiones críticas de disponibilidad. Priorizar arquitectura modular monolítica con Flutter para app móvil, Next.js para panel web administrativo, FastAPI en backend, PostgreSQL como base de datos, Redis para colas y jobs, y monorepo para coordinar desarrollo asistido por agentes.

### Prompt técnico base para Copilot

```text
Actúa como arquitecto y desarrollador senior trabajando dentro de un monorepo para una plataforma SaaS multi-tenant de captación y agendamiento. Sigue estrictamente estas reglas:

1. La arquitectura es modular monolítica, no microservicios.
2. Backend en FastAPI con separación por capas: API, Application, Domain, Infrastructure.
3. Frontend móvil en Flutter.
4. Panel web administrativo en Next.js con TypeScript.
5. PostgreSQL es la fuente de verdad.
6. Redis se usa para jobs, locks y cache ligera.
7. La agenda se resuelve por reglas internas, no por IA.
8. La IA solo sirve para intención, extracción, resumen y redacción.
9. Toda integración externa debe implementarse detrás de adaptadores.
10. Toda historia debe dejar código compilable, pruebas relevantes y notas de verificación.

Al generar código:
- respeta multi-tenancy en todas las entidades y queries
- evita lógica de negocio en controladores
- crea tipos y contratos explícitos
- agrega tests unitarios o de integración cuando aplique
- documenta supuestos técnicos dentro del código
- no inventes dependencias innecesarias
- si falta un dato, elige la opción más simple compatible con este PRD

Al finalizar cada tarea entrega:
- resumen de cambios
- archivos creados o modificados
- decisiones técnicas tomadas
- cómo validar localmente
- riesgos o follow-ups
```

---

## 25. Backlog de MVP por épicas e historias

Este backlog está diseñado para desarrollo asistido por agentes. Cada historia incluye:

* objetivo concreto
* Definition of Done (DoD)
* ready-to-paste Copilot prompt
* criterios de validación antes de pasar a la siguiente historia

### Convención general para todas las historias

#### Regla de ejecución para agentes

Antes de comenzar cualquier historia, el agente debe:

1. revisar este PRD
2. revisar la arquitectura técnica v1
3. limitar el trabajo al alcance de la historia actual
4. no introducir rediseños fuera de alcance
5. dejar el repositorio compilando y con pruebas relevantes

#### Plantilla de salida esperada de cada agente

Al terminar una historia, el agente debe devolver:

* resumen de cambios
* archivos creados/modificados
* decisiones técnicas tomadas
* cómo validar localmente
* riesgos o pendientes

---

## Épica 1. Fundación del monorepo y estándares de desarrollo

### Objetivo

Crear la base del repositorio, convenciones, tooling y estructura compartida para permitir trabajo paralelo seguro entre agentes.

### Historia 1.1 Crear estructura inicial del monorepo

**Objetivo**
Crear la estructura base del monorepo con apps, packages, infraestructura y documentación mínima.

**Definition of Done**

* existe estructura `/apps`, `/packages`, `/infrastructure`, `/docs`
* existen apps vacías o bootstrap para `mobile`, `web-admin`, `backend`
* existe README raíz con instrucciones básicas
* existe estrategia mínima de workspace compartido
* el repositorio instala dependencias y compila estructuras base sin errores

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 1.1 del PRD: crear la estructura inicial del monorepo para una plataforma SaaS multi-tenant.

Requisitos:
- crear carpetas /apps, /packages, /infrastructure, /docs
- preparar apps base para mobile (Flutter), web-admin (Next.js), backend (FastAPI)
- agregar README raíz con explicación breve del workspace
- preparar configuración mínima para desarrollo local
- mantener el alcance solo en bootstrap, sin lógica de negocio

Restricciones:
- no introducir microservicios
- no agregar dependencias innecesarias
- no implementar funcionalidades aún

Al finalizar entrega:
- resumen de cambios
- archivos creados/modificados
- cómo validar localmente
- pendientes
```

**Criterios de validación**

* el árbol del repo coincide con la arquitectura propuesta
* cada app puede inicializarse de forma independiente
* el README permite a otro dev arrancar el proyecto
* no hay carpetas ambiguas o duplicadas

---

### Historia 1.2 Configurar calidad base: lint, format y convenciones

**Objetivo**
Definir reglas de calidad consistentes para que los agentes trabajen con la misma base.

**Definition of Done**

* existe configuración de lint por stack
* existe formateo automático
* existe convención de nombres documentada
* existen scripts raíz para lint y format
* correr lint no produce errores en el bootstrap inicial

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 1.2 del PRD: configurar lint, format y convenciones base del monorepo.

Requisitos:
- agregar configuración de lint y format adecuada para backend, web-admin y packages TypeScript
- documentar convención de nombres y estructura
- crear scripts raíz para lint y format
- asegurar que el estado actual del repo quede limpio

Restricciones:
- no mezclar reglas de negocio
- no cambiar estructura del monorepo fuera de lo necesario
- mantener configuración simple y mantenible

Entrega final:
- resumen de cambios
- archivos modificados
- comandos para validar
- riesgos o follow-ups
```

**Criterios de validación**

* `lint` corre sin errores en lo ya implementado
* los scripts son simples y entendibles
* las convenciones quedan visibles para futuros agentes

---

### Historia 1.3 Configurar CI mínima

**Objetivo**
Asegurar verificación automática básica del repositorio.

**Definition of Done**

* existe pipeline CI mínima
* valida instalación, lint y pruebas base
* falla correctamente si hay error
* está documentada su intención

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 1.3 del PRD: configurar CI mínima para el monorepo.

Requisitos:
- agregar pipeline de integración continua básica
- validar instalación de dependencias, lint y pruebas disponibles
- mantener el pipeline simple y extensible
- documentar cómo funciona

Restricciones:
- no agregar despliegues todavía
- no agregar pasos pesados innecesarios

Al finalizar incluye:
- resumen de cambios
- archivos creados/modificados
- cómo validar la CI localmente o por simulación
```

**Criterios de validación**

* la CI corre sobre el repo base
* los checks reflejan salud real del proyecto
* no hay pasos frágiles sin justificación

---

## Épica 2. Backend base y arquitectura modular

### Objetivo

Crear el backend FastAPI con estructura por capas, health checks, configuración y base lista para módulos de dominio.

### Historia 2.1 Bootstrap del backend FastAPI modular

**Objetivo**
Inicializar backend con estructura por módulos y capas: API, Application, Domain, Infrastructure.

**Definition of Done**

* existe app FastAPI funcional
* existe estructura modular por dominio
* existe configuración por ambientes
* existe health endpoint
* el backend levanta localmente

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 2.1 del PRD: bootstrap del backend FastAPI con arquitectura modular monolítica.

Requisitos:
- crear app FastAPI con estructura por capas: API, Application, Domain, Infrastructure
- preparar módulos base aunque estén vacíos
- agregar endpoint /health
- agregar configuración por ambientes con defaults razonables
- dejar backend ejecutable localmente

Restricciones:
- no implementar lógica de negocio todavía
- no introducir dependencias no justificadas
- mantener separación de responsabilidades clara

Entrega:
- resumen de cambios
- estructura de carpetas creada
- comandos para correr backend
- pendientes detectados
```

**Criterios de validación**

* el backend arranca sin hacks
* la estructura refleja el diseño técnico
* `/health` responde correctamente
* configuración no está hardcodeada de forma peligrosa

---

### Historia 2.2 Configurar persistencia base y migraciones

**Objetivo**
Agregar conexión a PostgreSQL y mecanismo de migraciones.

**Definition of Done**

* existe conexión funcional a PostgreSQL
* existe base de migraciones
* existe configuración por ambiente
* existe prueba básica de conectividad
* la app puede iniciar con DB disponible

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 2.2 del PRD: persistencia base del backend con PostgreSQL y migraciones.

Requisitos:
- configurar acceso a PostgreSQL
- integrar herramienta de migraciones apropiada para FastAPI
- crear configuración inicial de conexión y primera migración vacía o base
- agregar prueba básica de conectividad o health dependiente de DB

Restricciones:
- no modelar aún todas las entidades del dominio
- no mezclar repositorios de negocio todavía

Entrega:
- resumen de cambios
- archivos creados/modificados
- cómo correr migraciones
- cómo validar conexión
```

**Criterios de validación**

* las migraciones corren hacia arriba y abajo
* la conexión a DB no depende de valores hardcodeados
* otro desarrollador puede levantar la base siguiendo instrucciones

---

### Historia 2.3 Configurar Redis y worker base

**Objetivo**
Preparar infraestructura asíncrona mínima para jobs.

**Definition of Done**

* existe conexión base a Redis
* existe worker inicial
* existe un job de prueba
* la app y el worker pueden ejecutarse localmente

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 2.3 del PRD: configurar Redis y worker base para tareas asíncronas.

Requisitos:
- agregar integración base con Redis
- crear worker inicial para jobs
- crear job simple de prueba o demo verificable
- documentar ejecución local

Restricciones:
- no implementar lógica compleja todavía
- mantener la integración lo más simple posible

Entrega:
- resumen de cambios
- archivos modificados
- cómo levantar worker y probar job
```

**Criterios de validación**

* Redis se conecta correctamente
* el worker procesa al menos un job simple
* la separación entre API y worker queda clara

---

## Épica 3. Multi-tenancy, autenticación y autorización

### Objetivo

Establecer aislamiento por tenant y acceso seguro por usuario y rol.

### Historia 3.1 Modelar Tenant y User

**Objetivo**
Crear entidades base de negocio para empresa y usuarios internos.

**Definition of Done**

* existen modelos y migraciones de Tenant y User
* existe relación entre ambos
* existen campos base según arquitectura
* hay pruebas básicas de persistencia

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 3.1 del PRD: modelar Tenant y User en el backend.

Requisitos:
- crear modelos, migraciones y repositorios base para Tenant y User
- incluir relación entre Tenant y User
- incluir campos base definidos en arquitectura técnica
- agregar pruebas básicas de persistencia o repositorio

Restricciones:
- no implementar autenticación completa todavía
- no agregar campos no justificados

Entrega:
- resumen de cambios
- archivos creados/modificados
- cómo validar persistencia localmente
```

**Criterios de validación**

* la base de datos refleja bien la relación tenant-usuarios
* las pruebas cubren creación y consulta básica
* todos los registros críticos incluyen tenant_id donde corresponde

---

### Historia 3.2 Implementar autenticación base

**Objetivo**
Permitir login seguro para usuarios internos.

**Definition of Done**

* existe flujo base de autenticación
* existe emisión/validación de token o sesión según decisión técnica
* existe endpoint de login
* existe endpoint para usuario actual
* existe manejo correcto de credenciales inválidas

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 3.2 del PRD: autenticación base para usuarios internos del sistema.

Requisitos:
- implementar login seguro para User
- exponer endpoint de autenticación
- exponer endpoint para obtener usuario autenticado actual
- manejar errores de credenciales inválidas
- documentar el enfoque elegido

Restricciones:
- mantener el diseño compatible con multi-tenancy
- no implementar SSO todavía
- no mezclar autorización avanzada aún

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar login localmente
- riesgos o follow-ups
```

**Criterios de validación**

* login exitoso funciona de punta a punta
* credenciales inválidas retornan error controlado
* el usuario autenticado pertenece a un tenant verificable

---

### Historia 3.3 Implementar autorización por rol y tenant

**Objetivo**
Restringir acceso por rol y por tenant.

**Definition of Done**

* existen roles iniciales
* existe middleware o dependencia de autorización
* no se permite acceso cross-tenant
* existen pruebas de acceso permitido y denegado

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 3.3 del PRD: autorización por rol y tenant en el backend.

Requisitos:
- definir roles iniciales del sistema
- implementar control de acceso por rol
- implementar aislamiento por tenant en endpoints protegidos
- agregar pruebas de acceso permitido y denegado

Restricciones:
- no duplicar lógica de autorización en cada endpoint si puede abstraerse
- mantener el diseño simple y auditable

Entrega:
- resumen de cambios
- decisiones técnicas tomadas
- cómo validar escenarios autorizados y no autorizados
```

**Criterios de validación**

* un usuario no puede leer o modificar datos de otro tenant
* los roles aplican correctamente en endpoints protegidos
* las pruebas cubren casos positivos y negativos

---

## Épica 4. Dominio de agenda y configuración operativa

### Objetivo

Implementar el núcleo del dominio de agenda: servicios, sucursales, prestadores, reglas de disponibilidad y citas.

### Historia 4.1 Modelar Service, Location y Provider

**Objetivo**
Crear entidades base para operar servicios agendables.

**Definition of Done**

* existen modelos y migraciones de Service, Location y Provider
* existe relación tenant-scoped
* existe relación ProviderLocation
* existen endpoints CRUD básicos protegidos
* hay pruebas básicas de repositorio o API

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 4.1 del PRD: modelar Service, Location y Provider con soporte multi-tenant.

Requisitos:
- crear entidades, migraciones y relaciones necesarias
- incluir tabla relacional ProviderLocation
- exponer CRUD básico protegido por autenticación y tenant
- agregar pruebas básicas

Restricciones:
- no implementar todavía reglas complejas de disponibilidad
- evitar campos innecesarios fuera del PRD

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar CRUD localmente
```

**Criterios de validación**

* las entidades se crean y consultan por tenant
* la relación provider-location funciona
* los endpoints protegidos respetan autorización

---

### Historia 4.2 Modelar AvailabilityRule y cálculo básico de slots

**Objetivo**
Implementar reglas persistidas de disponibilidad y cálculo simple de horarios.

**Definition of Done**

* existe entidad AvailabilityRule y migración
* existe lógica para calcular slots disponibles
* la lógica considera duración del servicio
* la lógica considera buffers base
* existen pruebas unitarias del cálculo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 4.2 del PRD: AvailabilityRule y cálculo básico de slots disponibles.

Requisitos:
- modelar AvailabilityRule según la arquitectura técnica
- implementar servicio de dominio o aplicación para calcular slots disponibles
- considerar duración del servicio y buffers
- soportar disponibilidad por sucursal y prestador opcional
- agregar pruebas unitarias para varios escenarios

Restricciones:
- no usar IA para resolver disponibilidad
- no integrar aún Google Calendar
- mantener el algoritmo simple, correcto y extensible

Entrega:
- resumen de cambios
- archivos modificados
- casos probados
- cómo validar cálculo localmente
```

**Criterios de validación**

* los slots generados respetan reglas y duración
* los tests cubren casos de sucursal y prestador
* no se generan slots fuera de rango

---

### Historia 4.3 Modelar Appointment y validación de conflictos

**Objetivo**
Permitir crear citas sin solapamientos inválidos.

**Definition of Done**

* existe entidad Appointment y migración
* existe caso de uso para crear cita
* valida conflictos por prestador o por sucursal según aplique
* registra origen y actor de creación
* existen pruebas unitarias o de integración para conflictos

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 4.3 del PRD: entidad Appointment y validación de conflictos al crear citas.

Requisitos:
- modelar Appointment con campos definidos en arquitectura
- implementar caso de uso de creación de cita
- validar conflictos por provider_id cuando exista
- validar conflictos por sucursal o capacidad base cuando provider_id no exista
- registrar metadata de creación
- agregar pruebas para creación válida y conflictos

Restricciones:
- no integrar aún calendario externo
- no introducir capacidad avanzada fuera del MVP
- mantener trazabilidad de decisiones

Entrega:
- resumen de cambios
- archivos modificados
- cómo probar creación y conflictos
```

**Criterios de validación**

* una cita válida se crea correctamente
* un conflicto bloquea la operación con error claro
* la auditoría o metadata mínima queda persistida

---

### Historia 4.4 Implementar reprogramación y cancelación básicas

**Objetivo**
Completar operaciones mínimas del ciclo de vida de la cita.

**Definition of Done**

* existen casos de uso para reprogramar y cancelar
* se validan conflictos al reprogramar
* se actualiza estado correctamente
* existen pruebas para ambos flujos

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 4.4 del PRD: reprogramación y cancelación básicas de citas.

Requisitos:
- crear casos de uso para reprogramar y cancelar Appointment
- validar conflictos al reprogramar
- actualizar estados de manera consistente
- dejar trazabilidad mínima del cambio
- agregar pruebas para ambos flujos

Restricciones:
- no integrar aún recordatorios complejos
- no introducir estados innecesarios

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar ambos flujos
```

**Criterios de validación**

* cancelar cambia el estado esperado
* reprogramar valida disponibilidad antes de guardar
* los tests cubren éxito y error

---

## Épica 5. Leads, contactos y conversaciones

### Objetivo

Implementar el núcleo de captación y seguimiento conversacional.

### Historia 5.1 Modelar Contact, Lead y Conversation

**Objetivo**
Persistir prospectos y sus conversaciones por canal.

**Definition of Done**

* existen modelos y migraciones de Contact, Lead y Conversation
* existen relaciones correctas
* existen repositorios o servicios base
* existen pruebas básicas de persistencia

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 5.1 del PRD: modelar Contact, Lead y Conversation.

Requisitos:
- crear entidades, migraciones y relaciones de Contact, Lead y Conversation
- respetar multi-tenancy
- incluir campos base definidos en arquitectura técnica
- agregar pruebas básicas de persistencia y asociación

Restricciones:
- no modelar todavía inbound webhook
- no implementar IA todavía

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar creación y relación entre entidades
```

**Criterios de validación**

* un lead puede asociarse a un contacto y múltiples conversaciones
* el tenant scope se conserva en todo momento
* las pruebas cubren el flujo básico de persistencia

---

### Historia 5.2 Modelar Message y endpoints internos de conversación

**Objetivo**
Registrar mensajes y exponer operaciones internas mínimas sobre conversaciones.

**Definition of Done**

* existe entidad Message y migración
* existe relación con Conversation
* existen endpoints internos para listar conversación y mensajes
* existen pruebas básicas de lectura

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 5.2 del PRD: modelar Message y endpoints internos de conversación.

Requisitos:
- crear entidad Message y su relación con Conversation
- exponer endpoints protegidos para listar conversaciones y mensajes
- permitir orden cronológico consistente
- agregar pruebas básicas de lectura

Restricciones:
- no implementar todavía integración con canales Meta
- no implementar envío real aún

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar lectura de conversaciones
```

**Criterios de validación**

* los mensajes se almacenan y leen correctamente
* las listas respetan tenant y conversación
* los resultados tienen orden consistente

---

### Historia 5.3 Implementar estados básicos de lead y handoff

**Objetivo**
Permitir clasificación operativa inicial y derivación a humano.

**Definition of Done**

* existe estado base para leads y conversaciones
* existe entidad o registro de Handoff
* existe caso de uso para derivar a humano
* existe notación resumida o metadata suficiente
* existen pruebas básicas del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 5.3 del PRD: estados básicos de lead y handoff a humano.

Requisitos:
- definir estados mínimos de Lead y Conversation
- modelar Handoff según la arquitectura técnica
- implementar caso de uso para derivar una conversación a humano
- registrar motivo y estado
- agregar pruebas del flujo

Restricciones:
- no implementar todavía notificaciones push reales
- mantener el flujo simple y auditable

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar el handoff localmente
```

**Criterios de validación**

* una conversación puede pasar a estado handoff
* el sistema registra motivo y responsable si aplica
* el flujo es auditable

---

## Épica 6. Integración con Meta: recepción y normalización

### Objetivo

Conectar canales Meta mediante webhooks, normalizar eventos y persistir mensajes entrantes.

### Historia 6.1 Implementar endpoint base de webhooks Meta

**Objetivo**
Recibir eventos entrantes y validar autenticidad básica según diseño adoptado.

**Definition of Done**

* existe endpoint de webhook Meta
* existe flujo de verificación requerido
* se reciben payloads de prueba
* se registran eventos sin procesar para depuración
* existen pruebas básicas del endpoint

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 6.1 del PRD: endpoint base de webhooks para canales Meta.

Requisitos:
- crear endpoint de webhook Meta en FastAPI
- soportar el flujo de verificación necesario
- aceptar payloads de prueba realistas
- registrar evento recibido para depuración inicial
- agregar pruebas básicas del endpoint

Restricciones:
- no implementar todavía procesamiento completo por canal
- no acoplar el webhook a lógica de dominio directamente

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar webhook localmente
```

**Criterios de validación**

* el endpoint responde correctamente a verificación
* acepta payloads de prueba sin romperse
* queda traza de recepción para diagnóstico

---

### Historia 6.2 Normalizar evento entrante y crear/actualizar conversación

**Objetivo**
Transformar payloads del webhook a un formato interno consistente.

**Definition of Done**

* existe adaptador de normalización por canal o base común
* se produce evento interno `message.received`
* se crea o actualiza Contact, Lead y Conversation según reglas mínimas
* se persiste Message inbound
* existen pruebas de integración del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 6.2 del PRD: normalización de evento entrante Meta y persistencia en dominio.

Requisitos:
- transformar payload webhook a un evento interno común
- crear o actualizar Contact, Lead y Conversation según reglas base
- persistir Message inbound asociado
- mantener aislamiento por tenant y canal
- agregar pruebas de integración del flujo

Restricciones:
- no implementar todavía respuestas automáticas avanzadas
- no mezclar IA en esta historia
- mantener el adaptador desacoplado del dominio

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar ingestión completa localmente
```

**Criterios de validación**

* un mensaje entrante genera o actualiza entidades correctas
* el formato interno es consistente entre canales
* las pruebas cubren al menos un caso inbound completo

---

### Historia 6.3 Implementar envío saliente básico por adaptador

**Objetivo**
Permitir enviar mensajes simples desde el sistema hacia el canal.

**Definition of Done**

* existe interfaz de envío saliente
* existe adaptador inicial por canal o uno mockeable
* se persiste Message outbound
* existe manejo básico de error y retry
* hay pruebas del servicio

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 6.3 del PRD: envío saliente básico de mensajes mediante adaptadores de canal.

Requisitos:
- definir interfaz para envío saliente de mensajes
- implementar adaptador inicial utilizable o mockeable
- persistir Message outbound con estado inicial
- agregar manejo básico de error y retry razonable
- agregar pruebas del servicio de envío

Restricciones:
- no implementar todavía plantillas complejas
- no mezclar lógica conversacional con el adaptador

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar envío o simulación localmente
```

**Criterios de validación**

* el sistema puede preparar y registrar un mensaje saliente
* el adaptador es intercambiable y testeable
* los errores no rompen el flujo principal sin control

---

## Épica 7. Asistente conversacional y automatización controlada

### Objetivo

Automatizar la primera respuesta, la captura de datos mínimos y la derivación controlada.

### Historia 7.1 Implementar respuesta automática inicial

**Objetivo**
Enviar una primera respuesta automática a nuevos leads o conversaciones según regla base.

**Definition of Done**

* existe regla para detectar cuándo enviar respuesta inicial
* se envía mensaje automático una sola vez por conversación según configuración
* se persiste el outbound correspondiente
* existen pruebas del comportamiento

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 7.1 del PRD: respuesta automática inicial para nuevos leads o conversaciones.

Requisitos:
- detectar cuándo corresponde enviar respuesta inicial automática
- evitar envíos duplicados no deseados
- persistir el mensaje saliente y su contexto
- agregar pruebas de comportamiento

Restricciones:
- no implementar todavía IA compleja
- mantener el contenido configurable o fácilmente reemplazable

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar respuesta inicial localmente
```

**Criterios de validación**

* una nueva conversación recibe respuesta inicial según regla
* no se envía repetidamente sin motivo
* el flujo queda trazable

---

### Historia 7.2 Implementar captura de datos mínimos por estado conversacional

**Objetivo**
Guiar la conversación para obtener nombre, servicio, sucursal, teléfono y horario preferido.

**Definition of Done**

* existe estado conversacional mínimo
* el sistema detecta datos faltantes
* solicita los faltantes en orden simple
* persiste los datos capturados en lead/contacto/contexto
* existen pruebas del flujo base

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 7.2 del PRD: captura de datos mínimos para agenda mediante estado conversacional controlado.

Requisitos:
- implementar un estado conversacional mínimo, sin depender todavía de IA compleja
- detectar qué datos faltan entre nombre, servicio, sucursal, teléfono y horario preferido
- solicitar datos faltantes de forma ordenada y simple
- persistir los datos capturados en las entidades adecuadas
- agregar pruebas del flujo base

Restricciones:
- no crear una máquina de estados excesivamente compleja
- no agendar aún sin pasar por el motor de disponibilidad

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar flujo conversacional localmente
```

**Criterios de validación**

* el sistema identifica faltantes correctamente
* los datos se guardan donde corresponde
* el flujo puede continuar hasta estar listo para ofrecer horarios

---

### Historia 7.3 Integrar IA controlada para intención y extracción

**Objetivo**
Agregar capa de IA con guardrails para intención, extracción y fallback.

**Definition of Done**

* existe interfaz de proveedor IA desacoplada
* existe caso de uso para intención y extracción
* existe validación de confianza
* existe fallback a flujo controlado o handoff
* existen pruebas o mocks del orquestador

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 7.3 del PRD: IA controlada para intención y extracción con guardrails.

Requisitos:
- crear interfaz desacoplada para proveedor de IA
- implementar caso de uso para detectar intención y extraer entidades relevantes
- validar nivel de confianza antes de ejecutar acciones importantes
- si la confianza es baja, usar fallback controlado o handoff
- agregar pruebas o mocks del orquestador

Restricciones:
- la IA no puede crear citas por sí sola
- la IA no puede decidir disponibilidad
- no comprometer cotizaciones automáticas

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar el flujo con mocks o proveedor configurado
- riesgos y pendientes
```

**Criterios de validación**

* la IA está detrás de una abstracción limpia
* confianza baja activa fallback en vez de acciones riesgosas
* el flujo sigue siendo auditable

---

## Épica 8. Agendamiento conversacional

### Objetivo

Conectar conversación, disponibilidad y creación de cita.

### Historia 8.1 Proponer horarios disponibles desde conversación

**Objetivo**
Usar los datos mínimos capturados para consultar slots y devolver opciones al lead.

**Definition of Done**

* existe caso de uso que consulta disponibilidad desde contexto conversacional
* devuelve opciones de horario consistentes
* registra el contexto de propuesta
* existen pruebas del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 8.1 del PRD: proponer horarios disponibles desde la conversación.

Requisitos:
- tomar datos mínimos capturados del contexto conversacional
- consultar el motor de disponibilidad
- devolver opciones de horario consistentes para el lead
- registrar el contexto de la propuesta realizada
- agregar pruebas del flujo

Restricciones:
- no crear la cita todavía en esta historia
- no usar IA para calcular disponibilidad

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar propuesta de horarios localmente
```

**Criterios de validación**

* solo se ofrecen horarios válidos
* las propuestas reflejan servicio, sucursal y opcionalmente prestador
* la conversación puede continuar hacia confirmación

---

### Historia 8.2 Confirmar elección y crear cita

**Objetivo**
Crear la cita a partir de la selección del lead.

**Definition of Done**

* existe caso de uso que interpreta la elección de horario
* vuelve a validar disponibilidad antes de crear
* crea Appointment
* genera mensaje de confirmación inicial
* existen pruebas del flujo exitoso y del conflicto de último momento

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 8.2 del PRD: confirmar elección de horario y crear la cita.

Requisitos:
- tomar la selección del lead sobre horarios propuestos
- revalidar disponibilidad inmediatamente antes de crear
- crear Appointment con trazabilidad completa
- generar mensaje de confirmación inicial
- agregar pruebas para éxito y conflicto de último momento

Restricciones:
- no integrar todavía calendario externo si no está listo
- no asumir que el horario sigue libre sin revalidar

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar creación desde conversación
```

**Criterios de validación**

* la cita queda creada solo si el slot sigue libre
* un conflicto tardío produce respuesta controlada
* el flujo completo deja evidencia trazable

---

### Historia 8.3 Implementar cancelación y reprogramación conversacional

**Objetivo**
Permitir al lead modificar su cita por mensajería.

**Definition of Done**

* existen intents o reglas para cancelar y reprogramar
* se conectan a los casos de uso del dominio
* la conversación informa resultado correctamente
* existen pruebas del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 8.3 del PRD: cancelación y reprogramación conversacional de citas.

Requisitos:
- detectar intención o comando de cancelar y reprogramar
- conectar el flujo conversacional con los casos de uso del dominio de Appointment
- responder al lead con resultado claro
- agregar pruebas del flujo

Restricciones:
- mantener el lenguaje de respuesta simple
- no introducir estados conversacionales innecesarios

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar ambos flujos localmente
```

**Criterios de validación**

* cancelar y reprogramar funcionan desde la conversación
* se respetan las reglas del dominio
* el usuario recibe retroalimentación clara

---

## Épica 9. Sincronización con Google Calendar

### Objetivo

Sincronizar citas del sistema con Google Calendar y detectar cambios relevantes.

### Historia 9.1 Conectar integración base de Google Calendar

**Objetivo**
Preparar conexión técnica y almacenamiento de credenciales/configuración.

**Definition of Done**

* existe adaptador base de Google Calendar
* existe configuración segura de credenciales
* existe modelo o configuración de conexión por tenant
* existen pruebas o mocks del adaptador

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 9.1 del PRD: integración base con Google Calendar.

Requisitos:
- crear adaptador base para Google Calendar detrás de una interfaz
- preparar manejo seguro de configuración y credenciales
- modelar conexión o configuración por tenant según necesidad del PRD
- agregar pruebas o mocks del adaptador

Restricciones:
- no sincronizar todavía todos los flujos completos
- no mezclar lógica de negocio dentro del adaptador

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar la integración o sus mocks
```

**Criterios de validación**

* el adaptador existe y es desacoplado
* la configuración no se expone de forma insegura
* otro desarrollador puede entender cómo conectarlo

---

### Historia 9.2 Sincronizar cita creada hacia Google Calendar

**Objetivo**
Crear evento externo cuando se crea una cita interna.

**Definition of Done**

* al crear Appointment se genera solicitud de sync
* existe job o proceso asíncrono de sync saliente
* se crea/actualiza CalendarSync
* existen pruebas del flujo o mocks verificables

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 9.2 del PRD: sincronización saliente de citas hacia Google Calendar.

Requisitos:
- emitir evento o job de sync al crear Appointment
- usar proceso asíncrono para crear evento externo
- persistir mapping en CalendarSync
- agregar pruebas o mocks verificables del flujo

Restricciones:
- no bloquear la creación de cita por la latencia externa si puede evitarse
- manejar errores con estado de sync razonable

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar sync saliente
```

**Criterios de validación**

* la creación de cita dispara sync
* el mapping con evento externo queda persistido
* un fallo externo no destruye la cita interna

---

### Historia 9.3 Procesar cambios inbound desde Google Calendar

**Objetivo**
Detectar cambios externos relevantes y reconciliar estado.

**Definition of Done**

* existe endpoint o flujo para cambios inbound
* existe reconciliación básica de evento externo a cita interna
* se registran inconsistencias detectables
* existen pruebas del flujo básico

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 9.3 del PRD: procesamiento de cambios inbound desde Google Calendar.

Requisitos:
- crear flujo o endpoint para recibir cambios externos relevantes
- reconciliar cambios contra Appointment y CalendarSync
- registrar inconsistencias o casos no resolubles automáticamente
- agregar pruebas del flujo básico

Restricciones:
- mantener la reconciliación simple para MVP
- no sobrescribir datos críticos sin validación mínima

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar cambios inbound o simulados
```

**Criterios de validación**

* el sistema puede detectar cambios externos relevantes
* las inconsistencias quedan trazables
* el comportamiento ante conflicto es controlado

---

## Épica 10. Recordatorios y confirmación de cita

### Objetivo

Implementar una confirmación automática por defecto.

### Historia 10.1 Modelar Reminder y job de confirmación

**Objetivo**
Programar y ejecutar una confirmación por cita.

**Definition of Done**

* existe entidad Reminder
* al crear cita se programa una confirmación
* existe worker que la procesa
* se actualiza estado de reminder
* existen pruebas del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 10.1 del PRD: Reminder y job de confirmación automática de cita.

Requisitos:
- modelar Reminder
- programar una confirmación automática al crear Appointment
- procesar la confirmación con worker
- actualizar estado del reminder y del mensaje asociado
- agregar pruebas del flujo

Restricciones:
- implementar solo una confirmación por defecto
- no agregar campañas de recordatorios múltiples todavía

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar recordatorio localmente
```

**Criterios de validación**

* toda cita nueva agenda una confirmación
* el worker la procesa correctamente
* el estado queda trazable

---

### Historia 10.2 Procesar respuesta de confirmación del lead

**Objetivo**
Actualizar la cita según la respuesta del usuario a la confirmación.

**Definition of Done**

* existe detección básica de confirmación del lead
* la cita cambia a estado confirmado cuando corresponde
* se contemplan respuestas no entendidas con fallback
* existen pruebas del flujo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 10.2 del PRD: procesar respuesta del lead a la confirmación de cita.

Requisitos:
- detectar respuesta de confirmación del lead en conversación
- actualizar Appointment al estado correspondiente cuando aplique
- manejar respuestas ambiguas con fallback o handoff
- agregar pruebas del flujo

Restricciones:
- no introducir lógica de NLP excesiva si una regla simple basta
- mantener trazabilidad del cambio de estado

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar confirmación del lead
```

**Criterios de validación**

* una respuesta válida confirma la cita
* respuestas ambiguas no rompen el sistema
* el cambio queda auditado o trazable

---

## Épica 11. Panel web administrativo básico

### Objetivo

Permitir onboarding, configuración y supervisión básica del negocio desde web.

### Historia 11.1 Bootstrap del panel web y autenticación

**Objetivo**
Inicializar el panel web y conectarlo al backend autenticado.

**Definition of Done**

* existe app Next.js funcional
* existe layout base
* existe login conectado al backend
* existe protección de rutas básicas
* el panel compila y corre localmente

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 11.1 del PRD: bootstrap del panel web administrativo y autenticación.

Requisitos:
- inicializar app Next.js con TypeScript
- crear layout base administrativo
- integrar login contra backend
- proteger rutas básicas autenticadas
- dejar app corriendo localmente

Restricciones:
- no construir todavía todas las pantallas del negocio
- mantener UI simple y funcional

Entrega:
- resumen de cambios
- archivos modificados
- cómo correr y validar web-admin
```

**Criterios de validación**

* el panel arranca correctamente
* login funciona con backend
* rutas protegidas no son accesibles sin autenticación

---

### Historia 11.2 Crear pantallas de configuración operativa

**Objetivo**
Permitir administrar servicios, sucursales y prestadores.

**Definition of Done**

* existen vistas CRUD básicas para Service, Location y Provider
* existe manejo de carga, error y estado vacío
* existe validación mínima de formularios
* existen pruebas básicas de componentes o integración

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 11.2 del PRD: pantallas web de configuración operativa.

Requisitos:
- crear vistas CRUD para Service, Location y Provider
- conectar con backend autenticado
- manejar loading, error y empty states
- agregar validación mínima de formularios
- agregar pruebas básicas de componentes o integración

Restricciones:
- mantener la UI sencilla y operativa
- no sobre-diseñar componentes visuales

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar CRUD desde el panel web
```

**Criterios de validación**

* un usuario puede gestionar configuración operativa desde web
* errores y vacíos son entendibles
* el panel refleja correctamente datos del tenant

---

### Historia 11.3 Crear vistas de agenda y conversaciones básicas

**Objetivo**
Dar visibilidad operativa mínima al negocio.

**Definition of Done**

* existe vista básica de agenda
* existe listado de conversaciones o leads
* existe navegación funcional entre vistas clave
* existen estados de carga y error

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 11.3 del PRD: vistas web básicas de agenda y conversaciones.

Requisitos:
- crear vista básica de agenda del negocio
- crear listado básico de conversaciones o leads
- conectar ambas vistas al backend
- manejar loading, error y empty states

Restricciones:
- no construir una suite operativa completa todavía
- priorizar claridad sobre complejidad visual

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar vistas operativas en web
```

**Criterios de validación**

* la operación puede consultar agenda y conversaciones desde web
* la navegación básica funciona
* la información visible corresponde al tenant autenticado

---

## Épica 12. App móvil operativa básica

### Objetivo

Permitir operación rápida desde móvil para conversaciones, agenda y handoff.

### Historia 12.1 Bootstrap de app Flutter y autenticación

**Objetivo**
Inicializar la app móvil y conectar autenticación.

**Definition of Done**

* existe app Flutter funcional
* existe navegación base
* existe pantalla login
* existe conexión con backend autenticado
* la app corre localmente o en simulador

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 12.1 del PRD: bootstrap de app Flutter y autenticación.

Requisitos:
- inicializar app Flutter
- crear navegación base
- crear pantalla de login
- conectar autenticación con backend
- dejar la app corriendo en simulador o entorno local

Restricciones:
- no implementar todavía toda la operación
- mantener estructura clara y escalable

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar la app localmente
```

**Criterios de validación**

* la app abre y permite login
* la navegación base funciona
* la autenticación persiste de forma razonable

---

### Historia 12.2 Crear bandeja de conversaciones y detalle

**Objetivo**
Permitir a operadores revisar conversaciones y mensajes desde móvil.

**Definition of Done**

* existe lista de conversaciones
* existe pantalla de detalle con mensajes
* existe refresco de datos
* existen estados de carga, error y vacío
* existen pruebas básicas de UI o integración

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 12.2 del PRD: bandeja móvil de conversaciones y detalle.

Requisitos:
- crear listado de conversaciones
- crear vista de detalle con mensajes
- conectar al backend autenticado
- manejar loading, error y empty states
- agregar pruebas básicas donde sea razonable

Restricciones:
- no implementar todavía composición avanzada de mensajes
- priorizar claridad y rapidez operativa

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar flujo móvil de conversaciones
```

**Criterios de validación**

* la bandeja muestra conversaciones del tenant correcto
* el detalle muestra mensajes ordenados
* la UX es suficientemente clara para operación básica

---

### Historia 12.3 Crear agenda del día y acciones rápidas

**Objetivo**
Dar acceso rápido a citas próximas y acciones operativas.

**Definition of Done**

* existe vista agenda del día
* existen acciones rápidas mínimas: ver, confirmar manualmente, handoff si aplica
* existen estados de carga y error
* la vista consume backend correctamente

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 12.3 del PRD: agenda del día y acciones rápidas en la app móvil.

Requisitos:
- crear vista de agenda del día
- mostrar citas próximas del tenant autenticado
- agregar acciones rápidas mínimas relevantes para operación
- manejar loading, error y empty states

Restricciones:
- no convertir la app móvil en panel administrativo complejo
- mantener el enfoque operativo rápido

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar agenda móvil localmente
```

**Criterios de validación**

* la agenda del día es usable
* las acciones rápidas funcionan sobre datos reales
* el alcance móvil se mantiene enfocado en operación

---

## Épica 13. Auditoría, métricas básicas y endurecimiento

### Objetivo

Agregar trazabilidad, métricas mínimas y criterios de cierre del MVP.

### Historia 13.1 Implementar AuditLog para acciones críticas

**Objetivo**
Registrar cambios sensibles del sistema.

**Definition of Done**

* existe entidad AuditLog
* acciones críticas generan registros de auditoría
* existe acceso básico a logs para diagnóstico
* existen pruebas del registro mínimo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 13.1 del PRD: auditoría de acciones críticas.

Requisitos:
- modelar AuditLog
- registrar acciones críticas como creación/cancelación de cita, cambios de disponibilidad y handoff
- exponer acceso básico para diagnóstico interno si ya hay soporte en backend
- agregar pruebas del registro mínimo

Restricciones:
- no construir un sistema analítico complejo
- mantener el formato de auditoría claro y consistente

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar auditoría localmente
```

**Criterios de validación**

* acciones críticas generan trazas consistentes
* los registros son consultables o inspeccionables
* el formato soporta diagnóstico real

---

### Historia 13.2 Exponer métricas básicas del MVP

**Objetivo**
Crear base mínima de reporting operativo.

**Definition of Done**

* existen endpoints o servicios de métricas básicas
* cubren leads recibidos, citas agendadas, confirmadas y canceladas
* existe definición clara del rango temporal
* existen pruebas básicas del cálculo

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 13.2 del PRD: métricas básicas del MVP.

Requisitos:
- exponer métricas básicas de leads y citas
- incluir al menos leads recibidos, citas agendadas, confirmadas y canceladas
- definir claramente filtros o rango temporal
- agregar pruebas básicas del cálculo

Restricciones:
- no construir BI avanzado
- priorizar consistencia y claridad de definiciones

Entrega:
- resumen de cambios
- archivos modificados
- cómo validar métricas localmente
```

**Criterios de validación**

* las métricas responden con definiciones consistentes
* los datos coinciden con fixtures o escenarios de prueba
* el MVP ya tiene visibilidad operativa mínima

---

### Historia 13.3 Cierre técnico de MVP

**Objetivo**
Verificar que el sistema cumple el mínimo viable y que se puede avanzar a beta controlada.

**Definition of Done**

* existe checklist final de validación técnica
* backend, web y mobile compilan
* pruebas críticas pasan
* flujos punta a punta mínimos están documentados
* se identifican pendientes no bloqueantes

**Ready-to-paste Copilot prompt**

```text
Implementa la historia 13.3 del PRD: cierre técnico del MVP.

Requisitos:
- crear checklist final de validación técnica del MVP
- verificar compilación de backend, web-admin y mobile
- verificar pruebas críticas
- documentar flujos punta a punta mínimos del sistema
- listar pendientes no bloqueantes para beta controlada

Restricciones:
- no abrir nuevas funcionalidades en esta historia
- enfocarse en consolidar y verificar

Entrega:
- resumen de cambios
- checklist final
- resultados de validación
- pendientes no bloqueantes
```

**Criterios de validación**

* el sistema compila en sus piezas principales
* los flujos mínimos existen y están verificables
* el equipo puede decidir paso a beta con base objetiva

---

## 26. Orden recomendado de ejecución

1. Épica 1. Fundación del monorepo
2. Épica 2. Backend base
3. Épica 3. Multi-tenancy y auth
4. Épica 4. Dominio de agenda
5. Épica 5. Leads y conversaciones
6. Épica 6. Integración Meta
7. Épica 7. Automatización conversacional
8. Épica 8. Agendamiento conversacional
9. Épica 9. Google Calendar
10. Épica 10. Confirmación
11. Épica 11. Panel web
12. Épica 12. App móvil
13. Épica 13. Auditoría, métricas y cierre

---

## 27. Regla para avanzar entre historias

No se debe avanzar a la siguiente historia si la actual no cumple simultáneamente:

* código compilable
* lint limpio o dentro del umbral acordado
* pruebas relevantes pasando
* validación local documentada
* sin romper historias ya cerradas

---

## 28. Criterio para iniciar desarrollo

Este documento es suficientemente sólido para iniciar:

* definición técnica de arquitectura
* diseño de dominio y modelo de datos
* wireframes principales
* backlog inicial de producto
* construcción del MVP

Con las decisiones ya tomadas, el siguiente paso recomendable es ejecutar este backlog por orden, usando una historia por vez y exigiendo evidencia de validación antes de continuar.

## 29. Recomendación operativa sobre el modelo de cita

La recomendación para v1 es modelar la cita con esta lógica:

* **Sucursal** como contexto obligatorio.
* **Prestador** como campo opcional pero soportado desde el inicio.
* **Recurso** como concepto interno extensible para fases posteriores.

### Justificación

Esto permite resolver tres escenarios comunes sin rediseñar el dominio:

1. Negocios donde solo importa la sucursal y cualquier persona disponible puede atender.
2. Negocios donde el lead quiere cita con un prestador específico.
3. Negocios donde la asignación fina puede hacerse después de captar la cita.

## 30. Recomendación operativa sobre panel web

La recomendación para v1 es incluir **panel web administrativo básico** además de la app móvil.

### Razón principal

La operación diaria ligera sí puede vivir en móvil, pero la configuración, supervisión y administración del negocio normalmente se resuelven mejor en web.

### Distribución recomendada

**App móvil**

* conversaciones
* agenda del día
* confirmaciones
* handoff
* notificaciones

**Panel web básico**

* configuración de servicios
* horarios
* sucursales
* prestadores
* usuarios
* conexiones de canal
* revisión de métricas
* administración general

### Beneficio

Esto evita forzar demasiada complejidad administrativa dentro de móvil y hace más viable la adopción por parte de negocios con operación real.

---

## 31. Master prompt operativo para Copilot Agent

Usar este prompt al inicio de cada historia o épica. Está diseñado para obligar disciplina, limitar alcance y dejar evidencia verificable.

### Master prompt base

```text
Actúa como un agente de desarrollo senior trabajando dentro de un monorepo para una plataforma SaaS multi-tenant de captación de leads desde Meta y agendamiento de citas.

Contexto obligatorio que debes respetar:
- arquitectura modular monolítica
- backend en FastAPI
- panel web en Next.js + TypeScript
- app móvil en Flutter
- PostgreSQL como fuente de verdad
- Redis para jobs, locks y cache ligera
- integraciones con canales Meta vía webhooks
- integración con Google Calendar para sincronización
- IA solo para intención, extracción, resumen y redacción
- la disponibilidad y la agenda se resuelven con reglas internas, no con IA
- multi-tenancy estricto en toda entidad y query

Tu misión es implementar SOLO la historia que yo te indique, sin expandir el alcance.

Reglas obligatorias de comportamiento:
1. Antes de cambiar código, resume en pocas líneas qué entendiste de la historia.
2. Identifica supuestos técnicos mínimos y no inventes funcionalidades fuera del PRD.
3. Si falta un detalle, elige la alternativa más simple compatible con la arquitectura.
4. No rediseñes módulos fuera del alcance actual.
5. No introduzcas microservicios, colas extra, librerías complejas o patrones innecesarios.
6. No mezcles lógica de negocio en controladores, widgets o páginas.
7. Toda integración externa debe ir detrás de una interfaz o adaptador.
8. Toda historia debe dejar el repositorio compilable.
9. Toda historia debe incluir pruebas razonables para su alcance.
10. Debes dejar evidencia clara de cómo validar localmente.

Forma de trabajo obligatoria:
1. Analiza la historia.
2. Lista los archivos que probablemente crearás o modificarás.
3. Ejecuta la implementación con el menor cambio posible que cumpla bien.
4. Corre o prepara validaciones locales.
5. Reporta exactamente qué cambió.
6. Señala riesgos, deuda técnica aceptada y pendientes.

Criterios de calidad obligatorios:
- código claro y mantenible
- nombres consistentes con el dominio del PRD
- contratos explícitos
- separación por capas
- sin lógica duplicada evitable
- manejo de errores razonable
- trazabilidad suficiente para depuración

Formato de salida obligatorio al terminar:
1. Resumen breve de implementación
2. Archivos creados
3. Archivos modificados
4. Decisiones técnicas tomadas
5. Cómo validar localmente
6. Resultado esperado de la validación
7. Riesgos o pendientes
8. Confirmación explícita de si la historia cumple o no cumple su DoD

Regla de cierre:
Si no puedes cumplir completamente el DoD, no declares la historia terminada. Indica exactamente qué falta.
```

### Variante para ejecutar una historia específica

```text
Usa el PRD y la arquitectura técnica del proyecto.

Implementa la siguiente historia sin salirte de su alcance:
[PEGAR AQUÍ LA HISTORIA COMPLETA CON OBJETIVO, DOD Y CRITERIOS]

Además del trabajo técnico, debes:
- respetar el master prompt operativo del proyecto
- no tocar historias futuras salvo que sea estrictamente necesario y lo justifiques
- dejar el código listo para revisión
- reportar si la historia queda cerrada o parcialmente cerrada
```

### Variante para continuar una historia ya iniciada

```text
Retoma una historia ya iniciada dentro del mismo proyecto.

Tu tarea es:
1. inspeccionar el estado actual del código relacionado con esta historia
2. comparar ese estado contra el DoD de la historia
3. completar solo lo faltante
4. evitar refactors amplios no pedidos
5. dejar evidencia clara de qué ya estaba, qué corregiste y qué falta

Historia objetivo:
[PEGAR AQUÍ LA HISTORIA]

Al final responde con:
- estado inicial encontrado
- cambios realizados en esta iteración
- validación ejecutada
- si ya cumple DoD o si sigue incompleta
```

---

## 32. Plantilla de revisión y checklist de auditoría por historia

Usar esta plantilla después de que el agente entregue una historia. La intención es decidir objetivamente si se puede avanzar o no.

### Plantilla de revisión

```text
Revisa la historia implementada usando el PRD, la arquitectura técnica y el DoD original.

Objetivo de la revisión:
Determinar si la historia realmente está terminada y si se puede avanzar a la siguiente sin arrastrar deuda oculta.

Instrucciones de revisión:
1. Compara el resultado entregado contra el objetivo de la historia.
2. Verifica cumplimiento exacto del DoD.
3. Verifica que no se haya expandido el alcance innecesariamente.
4. Verifica que el código compile o que exista evidencia razonable de compilación.
5. Verifica que existan pruebas acordes al alcance.
6. Verifica que la validación local esté explicada.
7. Detecta deuda técnica, omisiones o riesgos ocultos.
8. Decide si la historia queda:
   - APROBADA
   - APROBADA CON OBSERVACIONES
   - RECHAZADA

Devuelve tu respuesta con este formato:
- Resultado de revisión
- Cumplimiento del objetivo
- Cumplimiento del DoD punto por punto
- Calidad técnica observada
- Riesgos detectados
- Alcance extra introducido, si existe
- Validación local disponible
- Veredicto final
- Acciones necesarias antes de continuar
```

### Checklist corto de aprobación

#### A. Alcance

* [ ] La implementación corresponde exactamente a la historia.
* [ ] No se agregaron features fuera de alcance sin justificar.
* [ ] No se modificó arquitectura fuera de lo necesario.

#### B. Compilación y estabilidad

* [ ] El código compila o hay evidencia verificable de que compila.
* [ ] No se rompieron módulos previamente cerrados.
* [ ] No quedaron errores obvios sin documentar.

#### C. Calidad técnica

* [ ] La solución respeta la arquitectura definida.
* [ ] La lógica de negocio está en la capa correcta.
* [ ] Hay nombres, contratos y estructuras consistentes.
* [ ] Hay manejo razonable de errores.

#### D. Datos y seguridad

* [ ] Se respetó multi-tenancy.
* [ ] No hay accesos cross-tenant no controlados.
* [ ] No se expusieron secretos o configuraciones inseguras.

#### E. Validación

* [ ] Existen pruebas razonables para el alcance.
* [ ] Se describió cómo validar localmente.
* [ ] El resultado esperado de la validación está claro.

#### F. Cierre real

* [ ] El DoD se cumple completamente.
* [ ] Los pendientes están explícitos.
* [ ] Se puede avanzar sin arrastrar deuda bloqueante.

### Criterio de decisión

#### Aprobar

Aprobar solo si:

* el DoD está completo
* la validación es suficiente
* no hay defectos bloqueantes
* no se comprometió la arquitectura

#### Aprobar con observaciones

Usar solo si:

* la historia está funcionalmente completa
* hay detalles menores no bloqueantes
* esos detalles quedaron claramente documentados

#### Rechazar

Rechazar si:

* falta parte del DoD
* no hay evidencia de validación suficiente
* se introdujo deuda riesgosa
* se salió del alcance de forma dañina

---

## 33. Prompt de revisión del entorno de desarrollo y checklist de preparación

Este prompt sirve antes de arrancar historias. Su objetivo es verificar que el entorno, herramientas y prerequisitos estén listos para ejecutar el backlog sin fricción evitable.

### Prompt de revisión del entorno

```text
Actúa como arquitecto técnico y revisor de readiness de entorno para un proyecto SaaS multi-tenant con FastAPI, Next.js, Flutter, PostgreSQL, Redis, integraciones Meta y Google Calendar.

Tu objetivo es revisar si el entorno de desarrollo está listo para comenzar a ejecutar historias del backlog sin bloqueos previsibles.

Debes revisar y reportar lo siguiente:
1. estructura esperada del proyecto
2. herramientas base instaladas o requeridas
3. versiones recomendadas de runtime y SDKs
4. configuración local necesaria para backend, web y mobile
5. disponibilidad de PostgreSQL y Redis
6. estrategia de variables de entorno y secretos
7. capacidad para correr migraciones, pruebas, lint y builds
8. capacidad para trabajar integraciones externas con mocks o sandboxes
9. readiness para CI local o simulada
10. huecos o riesgos que bloquearían el inicio del backlog

Entrega el resultado en este formato:
- Estado general del entorno
- Herramientas requeridas
- Herramientas faltantes o por confirmar
- Configuración mínima necesaria
- Riesgos o bloqueos detectados
- Recomendaciones para quedar listo
- Checklist final de readiness
```

### Checklist detallado de herramientas y preparación

#### A. Sistema base

* [ ] Sistema operativo de desarrollo definido.
* [ ] Git instalado y funcionando.
* [ ] Acceso al repositorio confirmado.
* [ ] Permisos para clonar, crear ramas y hacer push.

#### B. Backend

* [ ] Python instalado en versión compatible con FastAPI y tooling elegido.
* [ ] Gestor de dependencias de Python definido.
* [ ] Entorno virtual o equivalente definido.
* [ ] Capacidad de correr backend localmente.
* [ ] Capacidad de correr migraciones.
* [ ] Capacidad de correr pruebas del backend.

#### C. Base de datos y cache

* [ ] PostgreSQL disponible localmente o en entorno accesible.
* [ ] Redis disponible localmente o en entorno accesible.
* [ ] Credenciales y puertos documentados para entorno local.
* [ ] Scripts o instrucciones de arranque disponibles.

#### D. Panel web

* [ ] Node.js instalado en versión compatible.
* [ ] Gestor de paquetes definido.
* [ ] Capacidad de correr Next.js localmente.
* [ ] Capacidad de correr lint y pruebas del panel.

#### E. App móvil

* [ ] Flutter SDK instalado.
* [ ] Dispositivo o simulador disponible.
* [ ] Capacidad de correr la app localmente.
* [ ] Capacidad de compilar al menos en modo debug.

#### F. Calidad y automatización

* [ ] Scripts de lint definidos.
* [ ] Scripts de test definidos.
* [ ] Scripts de build definidos.
* [ ] CI mínima definida o planeada.

#### G. Variables y secretos

* [ ] Existe estrategia para `.env` o equivalente.
* [ ] Secretos no se guardan en el repositorio.
* [ ] Variables mínimas del backend documentadas.
* [ ] Variables mínimas de web y mobile documentadas.

#### H. Integraciones externas

* [ ] Se definió cómo se simularán o mockearán webhooks de Meta al inicio.
* [ ] Se definió cómo se probará Google Calendar sin bloquear desarrollo.
* [ ] Existen credenciales sandbox o plan para agregarlas cuando toque.

#### I. Observabilidad y diagnóstico

* [ ] Logging básico disponible.
* [ ] Health checks previstos.
* [ ] Estrategia de depuración local definida.

#### J. Documentación inicial

* [ ] README raíz existe o está planeado.
* [ ] Instrucciones para levantar entorno local existen o están planeadas.
* [ ] Convenciones de trabajo para agentes están documentadas.

### Prompt corto para validar readiness antes de la primera historia

```text
Evalúa si el entorno de desarrollo está realmente listo para comenzar la Historia 1.1 del backlog.

Debes responder con:
1. listo / no listo
2. bloqueadores concretos
3. faltantes no bloqueantes
4. pasos exactos para quedar listo
5. checklist final marcado

No propongas desarrollo de producto todavía. Enfócate solo en readiness del entorno y herramientas.
```

---

## 34. Recomendación práctica de uso

### Secuencia sugerida

1. correr el prompt de readiness del entorno
2. cerrar bloqueadores técnicos
3. usar el master prompt operativo con una sola historia por vez
4. al terminar cada historia, pasar la plantilla de revisión
5. solo avanzar cuando la historia quede aprobada

### Regla de disciplina

Nunca pedirle al agente que implemente varias historias a la vez mientras el proyecto aún esté construyendo sus fundamentos.

### Regla de trazabilidad

Cada historia cerrada debe dejar:

* evidencia de compilación o validación
* lista de archivos modificados
* decisión de si cumple o no el DoD
* pendientes explícitos
