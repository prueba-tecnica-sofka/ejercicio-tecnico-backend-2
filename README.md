# ejercicio-tecnico-backend-2

> Prueba técnica
> Arquitectura Microservicios

## Estado del Proyecto ✅

El proyecto ha sido **completamente implementado** con todas las funcionalidades requeridas.

## Stack Utilizado

- **Backend**: Java 17 + Spring Boot 3.5.6
- **Base de Datos**: PostgreSQL 15
- **ORM**: JPA/Hibernate
- **Validación**: Jakarta Validation
- **Utilidades**: Lombok
- **Pruebas**: JUnit 5 + Mockito
- **Contenedorización**: Docker + Docker Compose
- **Build Tool**: Gradle

## Indicaciones Implementadas ✓

- ✓ Patrones Repository y buenas prácticas aplicadas
- ✓ Manejo de entidades con JPA/Hibernate
- ✓ Mensajes de excepciones personalizadas
- ✓ Pruebas unitarias de endpoints y servicios
- ✓ Solución desplegable en Docker

## Descripción del Proyecto

Implementación de una **API REST Bancaria** que maneja operaciones CRUD (GET, POST, PUT, DELETE) para las entidades del sistema bancario.

## Entidades Implementadas

### 👤 Persona
- **Atributos**: nombre, genero, edad, identificación, dirección, teléfono
- **Clave Primaria**: id (Long - auto-generado)
- **Enumeraciones**: Genero (MASCULINO, FEMENINO)

### 👨‍💼 Cliente
- **Hereda de**: Persona
- **Atributos adicionales**: clienteId, contraseña, estado
- **Clave Única**: clienteId (String - único)

### 💳 Cuenta
- **Atributos**: numeroCuenta, tipoCuenta, saldoInicial, estado, cliente
- **Clave Única**: numeroCuenta (String)
- **Enumeraciones**: TipoCuenta (AHORROS, CORRIENTE)
- **Relación**: Muchas cuentas pertenecen a un cliente

### 📝 Movimiento
- **Atributos**: fecha, tipoMovimiento, valor, saldo, cuenta
- **Clave Única**: id (Long - auto-generado)
- **Enumeraciones**: TipoMovimiento (DEPOSITO, RETIRO)
- **Relación**: Muchos movimientos pertenecen a una cuenta

## Funcionalidades Implementadas

### F1 - CRUDs (Crear, Leer, Actualizar, Eliminar)

#### Clientes (`/api/clientes`)
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

#### Cuentas (`/api/cuentas`)
- `POST /api/cuentas` - Crear cuenta
- `GET /api/cuentas` - Listar todas las cuentas
- `GET /api/cuentas/{id}` - Obtener cuenta por ID
- `PUT /api/cuentas/{id}` - Actualizar cuenta
- `DELETE /api/cuentas/{id}` - Eliminar cuenta

#### Movimientos (`/api/movimientos`)
- `POST /api/movimientos` - Registrar movimiento
- `GET /api/movimientos` - Listar movimientos
- `GET /api/movimientos/{id}` - Obtener movimiento por ID
- `PUT /api/movimientos/{id}` - Actualizar movimiento
- `DELETE /api/movimientos/{id}` - Eliminar movimiento

### F2 - Registro de Movimientos
- ✓ Movimientos con valores positivos (depósitos) o negativos (retiros)
- ✓ Actualización automática del saldo disponible en la cuenta
- ✓ Registro completo de transacciones realizadas

### F3 - Validación de Saldo
- ✓ Alerta `"Saldo no disponible"` cuando no hay fondos suficientes
- ✓ Excepción `SaldoInsuficienteException` personalizada

### F4 - Reportes
- `GET /api/reportes/movimientos` - Listar movimientos por cliente y fecha
- Respuesta incluye: cliente, número de cuenta, tipo de cuenta, movimiento, saldo disponible

## Componentes Implementados

### Entities (src/main/java/com/banco/api/entity/)
- `Persona.java` - Clase base con atributos comunes
- `Cliente.java` - Hereda de Persona
- `Cuenta.java` - Entidad de cuentas bancarias
- `Movimiento.java` - Entidad de movimientos
- `Genero.java` - Enum para género
- `TipoCuenta.java` - Enum para tipo de cuenta
- `TipoMovimiento.java` - Enum para tipo de movimiento

### Controllers (src/main/java/com/banco/api/controller/)
- `ClienteController.java` - Controlador de clientes
- `CuentaController.java` - Controlador de cuentas
- `MovimientoController.java` - Controlador de movimientos
- `ReporteController.java` - Controlador de reportes

### Services (src/main/java/com/banco/api/service/)
- `ClienteService.java` - Lógica de negocio de clientes
- `CuentaService.java` - Lógica de negocio de cuentas
- `MovimientoService.java` - Lógica de negocio de movimientos
- `ReporteService.java` - Lógica de reportes

### Repositories (src/main/java/com/banco/api/repository/)
- `ClienteRepository.java` - Acceso a datos de clientes
- `CuentaRepository.java` - Acceso a datos de cuentas
- `MovimientoRepository.java` - Acceso a datos de movimientos

### DTOs (src/main/java/com/banco/api/dto/)
- Request DTOs: Para recibir datos del cliente
- Response DTOs: Para enviar datos al cliente

### Exception Handling (src/main/java/com/banco/api/exception/)
- `GlobalExceptionHandler.java` - Manejo global de excepciones
- `SaldoInsuficienteException.java` - Excepción para saldo insuficiente
- `ResourceNotFoundException.java` - Excepción para recurso no encontrado
- `DuplicateResourceException.java` - Excepción para recursos duplicados
- `ErrorResponse.java` - Estructura de respuesta de error

### Mappers (src/main/java/com/banco/api/mapper/)
- `ClienteMapper.java` - Mapeo Cliente <-> DTOs
- `CuentaMapper.java` - Mapeo Cuenta <-> DTOs
- `MovimientoMapper.java` - Mapeo Movimiento <-> DTOs

## Pruebas Implementadas

### Unit Tests (src/test/java/com/banco/api/)
- `ClienteServiceTest.java` - Pruebas del servicio de clientes
- `MovimientoServiceTest.java` - Pruebas del servicio de movimientos
- `MovimientoControllerTest.java` - Pruebas del controlador de movimientos
- `RepositoryTest.java` - Pruebas de repositorios

## Configuración

### application.yml
```yaml
spring:
  application:
    name: api-bancaria
  datasource:
    url: jdbc:postgresql://localhost:5433/banco_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

server:
  port: 3000
```

## Instrucciones de Ejecución

### Prerequisitos
- Docker y Docker Compose instalados
- Git instalado
- (Opcional) Java 17 y Gradle para desarrollo local

### Opción 1: Ejecutar con Docker Compose (Recomendado)

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/ejercicio-tecnico-backend-2.git
cd ejercicio-tecnico-backend-2
```

2. **Ejecutar con Docker Compose**
```bash
docker-compose up -d
```

3. **Verificar que los servicios están corriendo**
```bash
docker-compose ps
```

4. **Acceder a la API**
```
http://localhost:3000/api/clientes
http://localhost:3000/api/cuentas
http://localhost:3000/api/movimientos
```

### Opción 2: Ejecutar Localmente (Desarrollo)

1. **Requisitos**
   - Java 17 JDK
   - PostgreSQL 15 corriendo en puerto 5433
   - Gradle (opcional, el proyecto incluye gradlew)

2. **Crear la base de datos**
```bash
psql -U postgres -h localhost -p 5433 -f docker/postgres/init/BaseDeDatos.sql
```

3. **Compilar el proyecto**
```bash
./gradlew clean build -x test
```

4. **Ejecutar la aplicación**
```bash
./gradlew bootRun
```

5. **La API estará disponible en**
```
http://localhost:3000
```

## Testing

### Ejecutar todas las pruebas
```bash
./gradlew test
```

### Ejecutar pruebas específicas
```bash
./gradlew test --tests ClienteServiceTest
./gradlew test --tests MovimientoControllerTest
```

## Casos de Uso (Ejemplos)

### 1. Crear un Cliente
```bash
curl -X POST http://localhost:3000/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Jose Lema",
    "genero": "MASCULINO",
    "edad": 28,
    "identificacion": "12345678",
    "direccion": "Otavalo sn y principal",
    "telefono": "098254785",
    "clienteId": "jose123",
    "contrasena": "1234",
    "estado": true
  }'
```

### 2. Crear una Cuenta
```bash
curl -X POST http://localhost:3000/api/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "478758",
    "tipoCuenta": "AHORROS",
    "saldoInicial": 2000,
    "estado": true,
    "clienteId": 1
  }'
```

### 3. Registrar un Movimiento
```bash
curl -X POST http://localhost:3000/api/movimientos \
  -H "Content-Type: application/json" \
  -d '{
    "tipoMovimiento": "RETIRO",
    "valor": 575,
    "cuentaId": 1
  }'
```

### 4. Listar Movimientos por Cliente
```bash
curl http://localhost:3000/api/reportes/movimientos
```

## Estructura de Respuesta de Error

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "path": "/api/movimientos"
}
```

## Base de Datos

### Script SQL (docker/postgres/init/BaseDeDatos.sql)
- Crea tablas con constraints adecuados
- Define relaciones entre entidades
- Inicializa datos de prueba

### Configuración Hibernate
- ddl-auto: validate (no modifica esquema en producción)
- Dialect: PostgreSQL
- Show SQL: true (para debugging)

## Logs

Los logs se escriben en:
- Consola (nivel DEBUG para com.banco.api)
- Archivo: `logs/` (si se configura)

## Estructura del Proyecto

```
.
├── src/
│   ├── main/
│   │   ├── java/com/banco/api/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/banco/api/
├── docker/
│   └── postgres/
│       └── init/
│           └── BaseDeDatos.sql
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── README.md
```

## Solución de Problemas

### Puerto 3000 ya en uso
```bash
# Cambiar puerto en docker-compose.yml o application.yml
```

### Conexión a PostgreSQL falla
```bash
# Verificar que PostgreSQL está corriendo
docker-compose ps postgres

# Ver logs de PostgreSQL
docker-compose logs postgres
```

### Limpiar todo y reiniciar
```bash
docker-compose down -v
docker-compose up -d
```

## Contacto y Soporte

Para reportar issues o preguntas, crear un issue en el repositorio.

---

**Última actualización**: Mayo 2026
**Versión**: 1.0.0
