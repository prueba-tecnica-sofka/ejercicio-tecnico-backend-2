# Plan Técnico - API REST Sistema Bancario

## Tabla de Contenidos

1. [Descripción General](#descripción-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Modelo de Datos](#modelo-de-datos)
4. [Especificación de APIs](#especificación-de-apis)
5. [Lógica de Negocio](#lógica-de-negocio)
6. [Implementación Técnica](#implementación-técnica)
7. [Testing](#testing)
8. [Despliegue](#despliegue)
9. [Guía de Desarrollo](#guía-de-desarrollo)

---

## Descripción General

### Objetivo
Desarrollar una API REST para gestión de cuentas bancarias que permita:
- Administrar clientes y sus cuentas
- Registrar movimientos financieros (depósitos/retiros)
- Consultar reportes de movimientos por fecha y cliente
- Validar saldos disponibles antes de transacciones

### Stack Tecnológico
- **Backend:** Java 17+ con Spring Boot 3.x
- **Base de Datos:** PostgreSQL 15+
- **ORM:** Spring Data JPA / Hibernate
- **Build:** Maven
- **Contenedorización:** Docker + Docker Compose

### Principios de Diseño
- **Clean Architecture** - Separación de responsabilidades
- **Repository Pattern** - Abstracción de acceso a datos
- **DTO Pattern** - Separación entre entidades y respuestas API
- **Exception Handling** - Manejo centralizado de errores
- **SOLID Principles** - Código mantenible y escalable

---

## Arquitectura del Sistema

### Estructura de Capas

```
src/
├── main/
│   ├── java/com/banco/api/
│   │   ├── controller/          # Capa de presentación
│   │   │   ├── ClienteController.java
│   │   │   ├── CuentaController.java
│   │   │   ├── MovimientoController.java
│   │   │   └── ReporteController.java
│   │   │
│   │   ├── service/             # Lógica de negocio
│   │   │   ├── ClienteService.java
│   │   │   ├── CuentaService.java
│   │   │   ├── MovimientoService.java
│   │   │   └── ReporteService.java
│   │   │
│   │   ├── repository/          # Acceso a datos
│   │   │   ├── ClienteRepository.java
│   │   │   ├── CuentaRepository.java
│   │   │   └── MovimientoRepository.java
│   │   │
│   │   ├── entity/              # Entidades JPA
│   │   │   ├── Persona.java
│   │   │   ├── Cliente.java
│   │   │   ├── Cuenta.java
│   │   │   └── Movimiento.java
│   │   │
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/
│   │   │   │   ├── ClienteRequestDTO.java
│   │   │   │   ├── CuentaRequestDTO.java
│   │   │   │   └── MovimientoRequestDTO.java
│   │   │   └── response/
│   │   │       ├── ClienteResponseDTO.java
│   │   │       ├── CuentaResponseDTO.java
│   │   │       ├── MovimientoResponseDTO.java
│   │   │       └── ReporteMovimientoDTO.java
│   │   │
│   │   ├── mapper/              # Conversión Entity <-> DTO
│   │   │   ├── ClienteMapper.java
│   │   │   ├── CuentaMapper.java
│   │   │   └── MovimientoMapper.java
│   │   │
│   │   ├── exception/           # Manejo de excepciones
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── SaldoInsuficienteException.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── ErrorResponse.java
│   │   │
│   │   └── config/              # Configuraciones
│   │       └── ApplicationConfig.java
│   │
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application-prod.yml
│
└── test/
    └── java/com/banco/api/
        ├── controller/
        ├── service/
        └── repository/
```

### Flujo de una Petición

```
Cliente HTTP → Controller → Service → Repository → Database
                    ↓           ↓
                   DTO      Validaciones
                              Lógica
```

---

## Modelo de Datos

### Diagrama de Relaciones

```
┌─────────────────┐
│     Persona     │ (Clase Base - @MappedSuperclass)
├─────────────────┤
│ - nombre        │
│ - genero        │
│ - edad          │
│ - identificacion│ (unique)
│ - direccion     │
│ - telefono      │
└─────────────────┘
         ▲
         │ (herencia)
         │
┌─────────────────┐           ┌─────────────────┐
│    Cliente      │ 1     n   │     Cuenta      │
├─────────────────┤───────────├─────────────────┤
│ PK clienteId    │           │ PK id           │
│    contraseña   │           │    numeroCuenta │ (unique)
│    estado       │           │    tipoCuenta   │ (enum)
└─────────────────┘           │    saldoInicial │
                              │    saldoActual  │
                              │    estado       │
                              │ FK clienteId    │
                              └─────────────────┘
                                       │
                                       │ 1
                                       │
                                       │ n
                              ┌─────────────────┐
                              │   Movimiento    │
                              ├─────────────────┤
                              │ PK id           │
                              │    fecha        │
                              │    tipoMovimiento│ (enum)
                              │    valor        │ (+ o -)
                              │    saldo        │ (después del mov)
                              │ FK cuentaId     │
                              └─────────────────┘
```

### Definición de Entidades

#### 1. Persona (Clase Base)

```java
@MappedSuperclass
@Getter
@Setter
public abstract class Persona {
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Genero genero;  // MASCULINO, FEMENINO, OTRO
    
    @Column(nullable = false)
    private Integer edad;
    
    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;
    
    @Column(length = 255)
    private String direccion;
    
    @Column(length = 20)
    private String telefono;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

#### 2. Cliente

```java
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;
    
    @Column(nullable = false)
    private String contraseña;  // En producción: usar BCrypt
    
    @Column(nullable = false)
    private Boolean estado = true;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuenta> cuentas = new ArrayList<>();
}
```

#### 3. Cuenta

```java
@Entity
@Table(name = "cuentas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String numeroCuenta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;  // AHORROS, CORRIENTE
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;
    
    @Column(nullable = false)
    private Boolean estado = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

#### 4. Movimiento

```java
@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento;  // DEPOSITO, RETIRO
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;  // Positivo para depósitos, negativo para retiros
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;  // Saldo después del movimiento
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
```

### Enums

```java
public enum Genero {
    MASCULINO, FEMENINO, OTRO
}

public enum TipoCuenta {
    AHORROS, CORRIENTE
}

public enum TipoMovimiento {
    DEPOSITO, RETIRO
}
```

---

## Especificación de APIs

### Base URL
```
http://localhost:3000/api
```

### 1. Clientes API

#### 1.1 Crear Cliente
```http
POST /api/clientes
Content-Type: application/json

{
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "contraseña": "1234",
  "estado": true
}

Response: 201 Created
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "identificacion": "1234567890",
  "estado": true,
  "createdAt": "2024-02-10T10:30:00"
}
```

#### 1.2 Listar Todos los Clientes
```http
GET /api/clientes

Response: 200 OK
[
  {
    "clienteId": 1,
    "nombre": "Jose Lema",
    "identificacion": "1234567890",
    "estado": true
  }
]
```

#### 1.3 Obtener Cliente por ID
```http
GET /api/clientes/{id}

Response: 200 OK
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "estado": true
}
```

#### 1.4 Actualizar Cliente
```http
PUT /api/clientes/{id}
Content-Type: application/json

{
  "nombre": "Jose Lema Updated",
  "direccion": "Nueva direccion 123",
  "telefono": "099999999"
}

Response: 200 OK
```

#### 1.5 Eliminar Cliente
```http
DELETE /api/clientes/{id}

Response: 204 No Content
```

### 2. Cuentas API

#### 2.1 Crear Cuenta
```http
POST /api/cuentas
Content-Type: application/json

{
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORROS",
  "saldoInicial": 2000.00,
  "estado": true,
  "clienteId": 1
}

Response: 201 Created
{
  "id": 1,
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORROS",
  "saldoInicial": 2000.00,
  "saldoActual": 2000.00,
  "estado": true,
  "cliente": {
    "clienteId": 1,
    "nombre": "Jose Lema"
  }
}
```

#### 2.2 Listar Cuentas
```http
GET /api/cuentas
GET /api/cuentas?clienteId=1

Response: 200 OK
```

#### 2.3 Obtener Cuenta por Número
```http
GET /api/cuentas/{numeroCuenta}

Response: 200 OK
```

#### 2.4 Actualizar Cuenta
```http
PUT /api/cuentas/{id}

Response: 200 OK
```

#### 2.5 Eliminar Cuenta
```http
DELETE /api/cuentas/{id}

Response: 204 No Content
```

### 3. Movimientos API

#### 3.1 Registrar Movimiento
```http
POST /api/movimientos
Content-Type: application/json

{
  "numeroCuenta": "478758",
  "tipoMovimiento": "RETIRO",
  "valor": 575.00
}

Response: 201 Created
{
  "id": 1,
  "fecha": "2024-02-10T15:30:00",
  "tipoMovimiento": "RETIRO",
  "valor": -575.00,
  "saldo": 1425.00,
  "numeroCuenta": "478758"
}

// Si saldo insuficiente:
Response: 400 Bad Request
{
  "timestamp": "2024-02-10T15:30:00",
  "status": 400,
  "error": "Saldo no disponible",
  "message": "La cuenta no tiene saldo suficiente para realizar el retiro",
  "path": "/api/movimientos"
}
```

#### 3.2 Listar Movimientos
```http
GET /api/movimientos
GET /api/movimientos?cuentaId=1

Response: 200 OK
```

#### 3.3 Obtener Movimiento por ID
```http
GET /api/movimientos/{id}

Response: 200 OK
```

### 4. Reportes API

#### 4.1 Reporte de Movimientos por Cliente y Fechas
```http
GET /api/reportes?clienteId=1&fechaInicio=2024-02-01&fechaFin=2024-02-28

Response: 200 OK
[
  {
    "fecha": "2024-02-10T00:00:00",
    "cliente": "Marianela Montalvo",
    "numeroCuenta": "225487",
    "tipo": "CORRIENTE",
    "saldoInicial": 100.00,
    "estado": true,
    "movimiento": 600.00,
    "saldoDisponible": 700.00
  }
]
```

---

## Lógica de Negocio

### Reglas de Movimientos

#### Algoritmo de Registro de Movimiento

```java
@Transactional
public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request) {
    // 1. Buscar la cuenta
    Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
        .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
    
    // 2. Calcular el valor del movimiento (negativo si es retiro)
    BigDecimal valor = request.getTipoMovimiento() == TipoMovimiento.RETIRO 
        ? request.getValor().negate() 
        : request.getValor();
    
    // 3. Calcular nuevo saldo
    BigDecimal nuevoSaldo = cuenta.getSaldoActual().add(valor);
    
    // 4. Validar saldo disponible (solo para retiros)
    if (request.getTipoMovimiento() == TipoMovimiento.RETIRO && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
        throw new SaldoInsuficienteException("Saldo no disponible");
    }
    
    // 5. Actualizar saldo de la cuenta
    cuenta.setSaldoActual(nuevoSaldo);
    cuentaRepository.save(cuenta);
    
    // 6. Crear y guardar el movimiento
    Movimiento movimiento = new Movimiento();
    movimiento.setFecha(LocalDateTime.now());
    movimiento.setTipoMovimiento(request.getTipoMovimiento());
    movimiento.setValor(valor);
    movimiento.setSaldo(nuevoSaldo);
    movimiento.setCuenta(cuenta);
    
    Movimiento saved = movimientoRepository.save(movimiento);
    
    // 7. Retornar respuesta
    return mapper.toResponseDTO(saved);
}
```

### Validaciones Importantes

1. **Saldo Inicial**: Debe ser >= 0
2. **Número de Cuenta**: Único en el sistema
3. **Identificación**: Única por cliente
4. **Retiro**: Saldo actual >= monto del retiro
5. **Eliminación de Cliente**: No permitir si tiene cuentas activas
6. **Estado de Cuenta**: No permitir movimientos en cuentas inactivas

### Transaccionalidad

- Todos los métodos de registro/actualización deben usar `@Transactional`
- Rollback automático ante excepciones
- Consistencia de saldos garantizada

---

## Implementación Técnica

### DTOs

#### ClienteRequestDTO
```java
@Data
public class ClienteRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El género es obligatorio")
    private Genero genero;
    
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    private Integer edad;
    
    @NotBlank(message = "La identificación es obligatoria")
    @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres")
    private String identificacion;
    
    private String direccion;
    private String telefono;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String contraseña;
    
    private Boolean estado = true;
}
```

### Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Resource Not Found")
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Saldo no disponible")
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```

### Configuración de Base de Datos

#### application.yml

```yaml
spring:
  application:
    name: api-bancaria
  
  datasource:
    url: jdbc:postgresql://localhost:5432/banco_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update  # En producción: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  jackson:
    serialization:
      write-dates-as-timestamps: false
    time-zone: America/Guayaquil

server:
  port: 3000
  servlet:
    context-path: /api

logging:
  level:
    com.banco.api: DEBUG
    org.hibernate.SQL: DEBUG
```

### Repositorios Personalizados

```java
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    
    List<Cuenta> findByClienteClienteId(Long clienteId);
    
    @Query("SELECT c FROM Cuenta c WHERE c.cliente.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findCuentasActivasByCliente(@Param("clienteId") Long clienteId);
}

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    List<Movimiento> findByCuentaId(Long cuentaId);
    
    @Query("SELECT m FROM Movimiento m " +
           "WHERE m.cuenta.cliente.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteAndFechas(
        @Param("clienteId") Long clienteId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
}
```

---

## Testing

### Estructura de Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class MovimientoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private MovimientoService movimientoService;
    
    @Test
    @DisplayName("Debe registrar un depósito exitosamente")
    void testRegistrarDeposito() throws Exception {
        // Given
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        request.setValor(new BigDecimal("600.00"));
        
        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setSaldo(new BigDecimal("2600.00"));
        
        when(movimientoService.registrarMovimiento(any())).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.saldo").value(2600.00));
    }
    
    @Test
    @DisplayName("Debe lanzar excepción cuando el saldo es insuficiente")
    void testRetiroConSaldoInsuficiente() throws Exception {
        // Given
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.RETIRO);
        request.setValor(new BigDecimal("5000.00"));
        
        when(movimientoService.registrarMovimiento(any()))
            .thenThrow(new SaldoInsuficienteException("Saldo no disponible"));
        
        // When & Then
        mockMvc.perform(post("/api/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Saldo no disponible"));
    }
}
```

### Tests de Servicio

```java
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {
    
    @Mock
    private MovimientoRepository movimientoRepository;
    
    @Mock
    private CuentaRepository cuentaRepository;
    
    @InjectMocks
    private MovimientoServiceImpl movimientoService;
    
    @Test
    void testCalculoSaldoRetiro() {
        // Given
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoActual(new BigDecimal("1000.00"));
        
        when(cuentaRepository.findByNumeroCuenta("478758"))
            .thenReturn(Optional.of(cuenta));
        
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.RETIRO);
        request.setValor(new BigDecimal("575.00"));
        
        // When
        movimientoService.registrarMovimiento(request);
        
        // Then
        verify(cuentaRepository).save(argThat(c -> 
            c.getSaldoActual().compareTo(new BigDecimal("425.00")) == 0
        ));
    }
}
```

### Coverage Objetivo
- **Clases de Servicio**: >80%
- **Controllers**: >70%
- **Repositorios**: >60%

---

## Despliegue

### Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: banco-postgres
    environment:
      POSTGRES_DB: banco_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./BaseDatos.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - banco-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  api:
    build: .
    container_name: banco-api
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/banco_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    ports:
      - "8080:8080"
    networks:
      - banco-network
    restart: unless-stopped

volumes:
  postgres_data:

networks:
  banco-network:
    driver: bridge
```

### BaseDatos.sql

```sql
-- Creación de base de datos
CREATE DATABASE banco_db;

-- Usar la base de datos
\c banco_db;

-- Tabla Clientes (incluye campos de Persona por herencia)
CREATE TABLE clientes (
    cliente_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INTEGER NOT NULL CHECK (edad >= 18),
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    contraseña VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabla Cuentas
CREATE TABLE cuentas (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE')),
    saldo_inicial NUMERIC(15,2) NOT NULL CHECK (saldo_inicial >= 0),
    saldo_actual NUMERIC(15,2) NOT NULL CHECK (saldo_actual >= 0),
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id) ON DELETE RESTRICT
);

-- Tabla Movimientos
CREATE TABLE movimientos (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_cuentas_cliente ON cuentas(cliente_id);
CREATE INDEX idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);
CREATE INDEX idx_clientes_identificacion ON clientes(identificacion);

-- Datos de prueba
INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, contraseña, estado)
VALUES 
    ('Jose Lema', 'MASCULINO', 35, '1234567890', 'Otavalo sn y principal', '098254785', '1234', true),
    ('Marianela Montalvo', 'FEMENINO', 28, '0987654321', 'Amazonas y NNUU', '097548965', '5678', true),
    ('Juan Osorio', 'MASCULINO', 42, '1122334455', '13 de junio y Equinoccial', '098874587', '1245', true);

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES 
    ('478758', 'AHORROS', 2000.00, 2000.00, true, 1),
    ('225487', 'CORRIENTE', 100.00, 100.00, true, 2),
    ('495878', 'AHORROS', 0.00, 0.00, true, 3),
    ('496825', 'AHORROS', 540.00, 540.00, true, 2);

-- Vista para reportes
CREATE VIEW v_reporte_movimientos AS
SELECT 
    m.fecha,
    c.nombre as cliente,
    cu.numero_cuenta,
    cu.tipo_cuenta as tipo,
    cu.saldo_inicial,
    cu.estado,
    m.valor as movimiento,
    m.saldo as saldo_disponible
FROM movimientos m
JOIN cuentas cu ON m.cuenta_id = cu.id
JOIN clientes c ON cu.cliente_id = c.cliente_id
ORDER BY m.fecha DESC;
```

### Comandos de Despliegue

```bash
# Construir y levantar servicios
docker-compose up -d --build

# Ver logs
docker-compose logs -f api

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

---

## Guía de Desarrollo

### Orden de Implementación Recomendado

#### Fase 1: Setup Inicial (1-2 horas)
1. ✅ Crear proyecto Spring Boot con dependencias
2. ✅ Configurar `application.yml`
3. ✅ Crear estructura de paquetes
4. ✅ Setup Docker y PostgreSQL

#### Fase 2: Modelo de Datos (2-3 horas)
5. ✅ Implementar entidades JPA
6. ✅ Crear enums
7. ✅ Configurar relaciones bidireccionales
8. ✅ Crear script `BaseDatos.sql`
9. ✅ Probar generación de schema

#### Fase 3: Capa de Datos (1 hora)
10. ✅ Implementar Repositories
11. ✅ Agregar queries personalizadas
12. ✅ Probar consultas básicas

#### Fase 4: DTOs y Mappers (1-2 horas)
13. ✅ Crear DTOs de Request
14. ✅ Crear DTOs de Response
15. ✅ Implementar Mappers
16. ✅ Agregar validaciones `@Valid`

#### Fase 5: Capa de Negocio (2-3 horas)
17. ✅ Implementar `ClienteService`
18. ✅ Implementar `CuentaService`
19. ✅ Implementar `MovimientoService` (lógica crítica)
20. ✅ Implementar `ReporteService`
21. ✅ Agregar `@Transactional`

#### Fase 6: Exception Handling (1 hora)
22. ✅ Crear excepciones personalizadas
23. ✅ Implementar `GlobalExceptionHandler`
24. ✅ Crear `ErrorResponse` DTO

#### Fase 7: Controllers (1-2 horas)
25. ✅ Implementar `ClienteController`
26. ✅ Implementar `CuentaController`
27. ✅ Implementar `MovimientoController`
28. ✅ Implementar `ReporteController`

#### Fase 8: Testing (2-3 horas)
29. ✅ Tests de `MovimientoController`
30. ✅ Tests de `MovimientoService`
31. ✅ Tests adicionales (opcional)

#### Fase 9: Documentación (1 hora)
32. ✅ Crear colección Postman
33. ✅ Exportar JSON de Postman
34. ✅ Actualizar README

#### Fase 10: Despliegue (1 hora)
35. ✅ Crear `Dockerfile`
36. ✅ Crear `docker-compose.yml`
37. ✅ Probar despliegue completo
38. ✅ Subir a repositorio Git

### Dependencias de Maven

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Checklist Final

- [ ] Todas las entidades tienen `@Entity` y relaciones configuradas
- [ ] Todos los endpoints CRUD funcionan correctamente
- [ ] Validación de saldo insuficiente implementada
- [ ] Manejo de excepciones centralizado
- [ ] Mínimo 2 tests unitarios pasando
- [ ] `BaseDatos.sql` con schema y datos iniciales
- [ ] Colección Postman exportada y funcional
- [ ] `Dockerfile` y `docker-compose.yml` funcionando
- [ ] README con instrucciones de despliegue
- [ ] Código subido a repositorio Git público

---

## Mejores Prácticas Aplicadas

### 1. Separación de Responsabilidades
- Controllers solo manejan HTTP
- Services contienen lógica de negocio
- Repositories solo acceso a datos

### 2. Uso de DTOs
- Nunca exponer entidades directamente en APIs
- Validaciones en capa de entrada
- Control de información sensible (contraseñas)

### 3. Manejo de Errores
- Excepciones específicas por caso de negocio
- Respuestas consistentes con `ErrorResponse`
- HTTP status codes apropiados

### 4. Transaccionalidad
- `@Transactional` en operaciones críticas
- Rollback automático ante errores
- Consistencia de datos garantizada

### 5. Validaciones
- Bean Validation (`@Valid`, `@NotNull`, etc.)
- Validaciones de negocio en Services
- Constraints a nivel de base de datos

### 6. Inmutabilidad
- DTOs de response son read-only
- Uso de `BigDecimal` para valores monetarios
- Fechas con `LocalDateTime` (no `Date`)

### 7. Documentación
- Nombres descriptivos de métodos
- JavaDoc en métodos públicos importantes
- README con ejemplos de uso

### 8. Seguridad (Mejoras Futuras)
- Encriptar contraseñas con BCrypt
- Implementar JWT para autenticación
- Validar permisos por rol

---

## Tiempo Estimado Total

**8-12 horas de desarrollo efectivo** distribuidas así:

- Setup y configuración: 1-2 horas
- Modelo de datos: 2-3 horas
- Lógica de negocio: 2-3 horas
- APIs y controllers: 1-2 horas
- Testing: 2-3 horas
- Documentación y despliegue: 1-2 horas

---

## Entregables Finales

1. ✅ **Código fuente** en repositorio Git público
2. ✅ **BaseDatos.sql** - Script de creación y datos
3. ✅ **Colección Postman** - Tests de endpoints
4. ✅ **README.md** - Instrucciones de despliegue
5. ✅ **Docker Compose** - Despliegue automatizado
6. ✅ **Tests unitarios** - Mínimo 2 pasando

---

**Documento generado para proyecto: API REST Sistema Bancario**  
**Fecha:** 2026  
**Stack:** Java Spring Boot + PostgreSQL + Docker
