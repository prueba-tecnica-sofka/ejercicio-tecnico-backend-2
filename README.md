# ejercicio-tecnico-backend-2

> Prueba técnica
> Arquitectura Microservicios

## Indicaciones generales

- Aplique todas las buenas prácticas, patrones Repository, etc que considere necesario
(se tomará en cuenta este punto para la calificación).
- El manejo de entidades se debe manejar JPA / Entity Framework Core
- Se debe manejar mensajes de excepciones.
- Se debe realizar como mínimo dos pruebas unitarias de los endpoints.
- La solución se debe desplegar en Docker.

## Stack

- Java Spring Boot
- PostgreSQL
- Postman (Validador de la API)

## Descripción

`Generación de API REST`

Maneja los verbos: GET,POST, PUT, PUSH AND DELETE para cada categoría para las siguientes entidades

### Persona

- Implementar la clase persona con los siguientes datos: nombre, genero, edad,
identificación, dirección, teléfono
- Debe manera su clave primaria (PK)

### Cliente

- Cliente debe manejar una entidad, que herede de la clase persona.
- Un cliente tiene: clienteid, contraseña, estado.
- El cliente debe tener una clave única. (PK)

### Cuenta

- Cuenta debe manejar una entidad
- Una cuenta tiene: número cuenta, tipo cuenta, saldo Inicial, estado.
- Debe manejar su Clave única

### Movimientos

- Movimientos debe manejar una sola entidad
- Un movimiento tiene: Fecha, tipo movimiento, valor, saldo
- Debe manejar su Clave única

## Funcionaliddades de la API

Las API deben tener las siguientes operaciones


`F1` Generación de CRUDS (Crear, editar, actualizar y eliminar registros - Entidades: Cliente,
Cuenta y Movimiento).

Los nombres de los endpoints a generar son:
    
- /cuentas
- /clientes
- /movimientos

`F2` Registro de movimientos: al registrar un movimiento en la cuenta se debe tener en cuenta
lo siguiente:

- Para un movimiento se pueden tener valores positivos o negativos.
- Al realizar un movimiento se debe actualizar el saldo disponible.
- Se debe llevar el registro de las transacciones realizadas

`F3` Registro de movimientos: Al realizar un movimiento el cual no cuente con saldo, debe
alertar mediante el siguiente mensaje “Saldo no disponible”

## Casos de uso (ejemplos)

1. Creación de Usuarios

| **Nombres**          | **Dirección**          | **Teléfono** | **Contraseña** | **estado** |
|----------------------|------------------------|--------------|----------------|------------|
| Jose Lema            | Otavalo sn y principal | 098254785    | 1234           | true       |
| Marianela Montalvo   | Otavalo sn y principal | 098254785    | 5678           | true       |
| Juan Osorio          | Otavalo sn y principal | 098254785    | 1245           | true       |

2. Creación de cuentas de usuario

| **Numero cuenta**    | **Tipo**    | **Saldo inicial** | **Estado**     | **Cliente**               |
|----------------------|-------------|-------------------|----------------|---------------------------|
| 478758               | Ahorros     | 2000              | true           | Jose Lema                 |
| 225487               | Corriente   | 100               | true           | Marianela Montalvo        |
| 495878               | Ahorros     | 0                 | true           | Juan Osorio               |
| 496825               | Ahorros     | 540               | true           | Marianela Montalvo        |

3. Crea una nueva cuenta corrienta para `Jose Lema`

| **Numero cuenta**    | **Tipo**    | **Saldo inicial** | **Estado**     | **Cliente**               |
|----------------------|-------------|-------------------|----------------|---------------------------|
| 585545               | Corriente   | 1000              | true           | Jose Lema                 |

4. Realizar los siguientes movimientos

| Número Cuenta | Tipo      | Saldo Inicial  | Estado    | Movimiento     |
|---------------|-----------|----------------|---------|------------------|
| 478758        | Ahorro    | 2000           | True    | Retiro de 575    |
| 225487        | Corriente | 100            | True    | Deposito de 600  |
| 495878        | Ahorros   | 0              | True    | Deposito de 150  |
| 496825        | Ahorros   | 540            | True    | Retiro de 540    |


5. Listado de Movimiento, por fechas x usuario

| Fecha     | Cliente            | Número Cuenta | Tipo      | Saldo Inicial  | Estado  | Movimiento  | Saldo Disponible  |
|-----------|--------------------|---------------|-----------|----------------|---------|-------------|-------------------|
| 10/2/2022 | Marianela Montalvo | 225487        | Corriente | 100            | True    | 600         | 700               |
| 8/2/2022  | Marianela Montalvo | 496825        | Ahorros   | 540            | True    | -540        | 0                 |

Ejemplo json

```json
{
"Fecha":"10/2/2022",
"Cliente":"Marianela Montalvo",
"Numero Cuenta":"225487"
"Tipo":"Corriente",
"Saldo Inicial":100,
"Estado":true,
"Movimiento":600,
"Saldo Disponible":700
}
```

## Instrucciones de despliegue

- Generar el script de base datos, entidades y esquema datos, con el nombre
BaseDatos.sql.
- Ejecutar Postman para poder realizar las verificaciones (http://{servidor}:{puerto}/api/{metodo}...{Parámetros}) 

## Despliegue

- La Solución debe ser cargado a un repositorio Git público, se debe enviar la ruta de
este repositorio.
- Descarga archivo Json, de Aplicación Postman, para validación de los endpoints.
- Se debe entregar antes de la fecha y hora indicada por correo.
