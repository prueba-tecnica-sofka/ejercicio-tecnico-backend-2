# Plan de Pruebas Unitarias - API Bancaria

## URLs para Postman

**Base URL:** `http://localhost:3000`

| Recurso | Método | URL |
|---|---|---|
| Crear cliente | POST | `http://localhost:3000/api/clientes` |
| Listar clientes | GET | `http://localhost:3000/api/clientes` |
| Obtener cliente | GET | `http://localhost:3000/api/clientes/{id}` |
| Actualizar cliente | PUT | `http://localhost:3000/api/clientes/{id}` |
| Eliminar cliente | DELETE | `http://localhost:3000/api/clientes/{id}` |
| Crear cuenta | POST | `http://localhost:3000/api/cuentas` |
| Listar cuentas | GET | `http://localhost:3000/api/cuentas` |
| Obtener cuenta | GET | `http://localhost:3000/api/cuentas/{numeroCuenta}` |
| Actualizar cuenta | PUT | `http://localhost:3000/api/cuentas/{id}` |
| Eliminar cuenta | DELETE | `http://localhost:3000/api/cuentas/{id}` |
| Registrar movimiento | POST | `http://localhost:3000/api/movimientos` |
| Listar movimientos | GET | `http://localhost:3000/api/movimientos` |
| Obtener movimiento | GET | `http://localhost:3000/api/movimientos/{id}` |
| Reporte por cliente/fechas | GET | `http://localhost:3000/api/reportes?clienteId=1&fechaInicio=2024-01-01&fechaFin=2024-12-31` |