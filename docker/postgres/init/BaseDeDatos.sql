-- Nota: en Docker, el entrypoint ejecuta este script sobre la base definida por
-- POSTGRES_DB (en este proyecto: banco_db). No se debe crear/cambiar de base aquí.

-- Tabla Clientes (incluye campos de Persona por herencia)
CREATE TABLE IF NOT EXISTS clientes (
    cliente_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INTEGER NOT NULL CHECK (edad >= 18),
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabla Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id BIGSERIAL PRIMARY KEY,
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
CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Índices para mejorar rendimiento
CREATE INDEX IF NOT EXISTS idx_cuentas_cliente ON cuentas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_fecha ON movimientos(fecha);
CREATE INDEX IF NOT EXISTS idx_clientes_identificacion ON clientes(identificacion);

-- Datos de prueba
INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES 
    ('Carlos Ruiz', 'MASCULINO', 35, '2234567890', 'Otavalo sn y principal', '098254785', '1234', true),
    ('Marianela Montalvo', 'FEMENINO', 28, '0987654321', 'Amazonas y NNUU', '097548965', '5678', true),
    ('Juan Osorio', 'MASCULINO', 42, '1122334455', '13 de junio y Equinoccial', '098874587', '1245', true);

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES 
    ('478758', 'AHORROS', 2000.00, 2000.00, true, 1),
    ('225487', 'CORRIENTE', 100.00, 100.00, true, 2),
    ('495878', 'AHORROS', 0.00, 0.00, true, 3),
    ('496825', 'AHORROS', 540.00, 540.00, true, 2);

-- Vista para reportes
CREATE OR REPLACE VIEW v_reporte_movimientos AS
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