-- ═══════════════════════════════════════════════
--  TUTA WAYTA — Script de Base de Datos
--  Contenedor Docker: pitahaya_mysql (puerto 3307)
-- ═══════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS tuta_wayta_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tuta_wayta_db;

-- ── Tabla: productos ──────────────────────────
CREATE TABLE IF NOT EXISTS productos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock       INT           NOT NULL DEFAULT 0,
    estado      VARCHAR(20)   NOT NULL DEFAULT 'Activo',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla: clientes ───────────────────────────
CREATE TABLE IF NOT EXISTS clientes (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    apellido  VARCHAR(100),
    email     VARCHAR(150),
    telefono  VARCHAR(20),
    estado    VARCHAR(20) NOT NULL DEFAULT 'Activo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla: pedidos ────────────────────────────
CREATE TABLE IF NOT EXISTS pedidos (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    cliente   VARCHAR(200) NOT NULL,
    producto  VARCHAR(200) NOT NULL,
    cantidad  INT           NOT NULL DEFAULT 1,
    total     DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado    VARCHAR(30)   NOT NULL DEFAULT 'Pendiente',
    fecha     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

-- ── Datos de ejemplo ──────────────────────────
INSERT INTO productos (nombre, descripcion, precio, stock, estado) VALUES
('Pitahaya Roja 1kg',  'Pitahaya fresca premium, cosecha del día',   12.50, 50, 'Activo'),
('Pitahaya Amarilla',  'Variedad amarilla, sabor intenso y dulce',   15.00, 30, 'Activo'),
('Jugo de Pitahaya',   'Jugo natural 500ml sin preservantes',         8.00, 100,'Activo'),
('Mermelada Pitahaya', 'Mermelada artesanal de pitahaya, 250g',      10.00, 20, 'Activo');

INSERT INTO clientes (nombre, apellido, email, telefono, estado) VALUES
('Ana',    'García',   'ana.garcia@email.com',   '987654321', 'Activo'),
('Carlos', 'López',    'carlos.lopez@email.com', '912345678', 'Activo'),
('María',  'Quispe',   'maria.q@email.com',      '934567890', 'Activo');

INSERT INTO pedidos (cliente, producto, cantidad, total, estado) VALUES
('Ana García',   'Pitahaya Roja 1kg', 3, 37.50, 'Entregado'),
('Carlos López', 'Jugo de Pitahaya',  5, 40.00, 'En proceso'),
('María Quispe', 'Mermelada Pitahaya',2, 20.00, 'Pendiente');
