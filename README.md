# TP Integrador · DAW1 — Eventos

**Alumno/a:** Vega, Franco · **Legajo:** 510015
**Enunciado elegido:** eventos


## Descripción

Sistema backend para una productora que permite vender entradas para eventos, controlando estrictamente la capacidad de la sala y previniendo reservas duplicadas por asistente.
## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

```bash
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080/api`. La base H2 se guarda en `data/` (no se sube al repositorio).
Consola H2: `http://localhost:8080/h2-console`.

## Modelo

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| Evento | id, nombre, fecha, capacidad, precio |  |
| Asistente | id, nombre, email |  |
| Entrada | id, estadofechaReserva, importePagado | N a 1 con Evento y Asistente |

Estados de Entrada: `RESERVADA → PAGADA → USADA | ANULADA. PAGADA → ANULADA`
## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/eventos | Lista eventos (`?nombre=rock` filtra por nombre) | 200 |
| GET | /api/eventos/{id} | Busca evento por id | 200, 404 |
| POST | /api/eventos | Crea un evento | 201, 400 |
| PUT | /api/eventos/{id} | Modifica un evento | 200, 400, 404 |
| DELETE | /api/eventos/{id} | Borra un evento | 204, 404, 409 |
| GET | /api/eventos/{id}/entradas | Entradas de un evento | 200, 404 |
| GET | /api/eventos/{id}/ocupacion | Ocupación (Extra) | 200 |
| GET | /api/asistentes | Lista los asistentes | 200 |
| GET | /api/asistentes/{id} | Busca un asistente por id | 200, 400, 404 |
| POST | /api/asistentes | Crea un asistente | 201, 400 |
| PUT | /api/asistentes/{id} | Modifica un asistente | 200, 400, 404 |
| DELETE | /api/asistentes/{id} | Borra un asistente sin entradas | 204, 404, 409 |
| POST | /api/entradas | Crea una entrada (eventoId, asistenteId) | 201, 400, 404, 409 |
| PATCH | /api/entradas/{id}/estado | Cambia el estado | 200, 400, 404, 409 |
| POST | /api/entradas/validar | Validar código UUID (Extra) | 200, 400, 404, 409 |
| PATCH | /api/entradas/{id}/transferir | Transferir entrada (Extra) | 200, 400, 404, 409 |

## Reglas de negocio

1. No se venden más entradas no anuladas que la capacidad del evento (devuelve 409).
2. Un asistente no puede tener dos entradas activas para el mismo evento (devuelve 409).
3. Un evento que tiene entradas no se puede borrar (devuelve 409).

## Extras implementados

* **Código de entrada:** Cada entrada recibe un código único (UUID). POST /api/entradas/validar la pasa a USADA. Devuelve 409 si no está pagada o ya se usó.
* **Preventa:** Al pasar una entrada a PAGADA, si se realiza el pago con más de 7 días de anticipación al evento, el importePagado se guarda con un 20% de descuento.
* **Ocupación:** GET /api/eventos/{id}/ocupacion devuelve la cantidad de entradas vendidas, disponibles y el porcentaje de ocupación.
* **Transferencia:** PATCH /api/entradas/{id}/transferir cambia el titular de una entrada PAGADA, validando la regla 2 para el nuevo asistente.
* **Anulación automática:** Tarea en segundo plano (@Scheduled) que anula automáticamente las entradas RESERVADA si superan las 48 horas sin ser pagadas.

## Pruebas

En `pruebas.http` están todas las peticiones de ejemplo (se ejecutan desde IntelliJ).