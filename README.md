# TP Integrador · DAW1 — Eventos_pro

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
| Entrada | id, estado | N a 1 con Evento y Asistente |

Estados de Entrada: `RESERVADA → PAGADA → USADA | ANULADA. PAGADA → ANULADA`
## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/eventos | Lista eventos | 200 |
| GET | /api/eventos/{id} | Busca evento por id | 200, 404 |
| POST | /api/eventos | Crea un evento | 201, 400 |
| PUT | /api/eventos/{id} | Modifica un evento | 200, 400, 404 |
| DELETE | /api/eventos/{id} | Borra un evento | 204, 404, 409 |
| GET | /api/eventos/{id}/entradas | Entradas de un evento | 200, 404 |
| GET | /api/eventos/{id}/ocupacion | Ocupación (Extra) | 200 |
| POST | /api/entradas | Crea una entrada | 201, 400, 404, 409 |
| PATCH | /api/entradas/{id}/estado | Cambia el estado | 200, 400, 404, 409 |
| POST | /api/entradas/validar | Validar código UUID (Extra) | 200, 400, 404, 409 |
| PATCH | /api/entradas/{id}/transferir | Transferir entrada (Extra) | 200, 400, 404, 409 |

## Reglas de negocio

1. No se venden más entradas no anuladas que la capacidad del evento (devuelve 409).
2. Un asistente no puede tener dos entradas activas para el mismo evento (devuelve 409).
3. Un evento que tiene entradas no se puede borrar (devuelve 409).

## Pruebas

En `pruebas.http` están todas las peticiones de ejemplo (se ejecutan desde IntelliJ).
