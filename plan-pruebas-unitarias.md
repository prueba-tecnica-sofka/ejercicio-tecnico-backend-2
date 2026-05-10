# Plan de Pruebas Unitarias - API Bancaria

## URLs para Postman

**Base URL:** `http://localhost:3000`

| Recurso | Método | URL |
|---|---|---|
| Crear cliente | POST | `http://localhost:3000/api/clientes` | ok
| Listar clientes | GET | `http://localhost:3000/api/clientes` | ok
| Obtener cliente | GET | `http://localhost:3000/api/clientes/{id}` |ok
| Actualizar cliente | PUT | `http://localhost:3000/api/clientes/{id}` | ok
| Eliminar cliente | DELETE | `http://localhost:3000/api/clientes/{id}` | ok
| Crear cuenta | POST | `http://localhost:3000/api/cuentas` |  ok
| Listar cuentas | GET | `http://localhost:3000/api/cuentas` | ok
| Obtener cuenta | GET | `http://localhost:3000/api/cuentas/{numeroCuenta}` | ok
| Actualizar cuenta | PUT | `http://localhost:3000/api/cuentas/{id}` | ok
| Eliminar cuenta | DELETE | `http://localhost:3000/api/cuentas/{id}` | ok
| Registrar movimiento | POST | `http://localhost:3000/api/movimientos` | ok
| Listar movimientos | GET | `http://localhost:3000/api/movimientos` | ok
| Obtener movimiento | GET | `http://localhost:3000/api/movimientos/{id}` | ok
| Reporte por cliente/fechas | GET | `http://localhost:3000/api/reportes?clienteId=1&fechaInicio=2024-01-01&fechaFin=2024-12-31` |


## Anotaciones

- Los movimientos en los saldos se corresponden correctamente a efectos del registro de movimientos en una cuenta
