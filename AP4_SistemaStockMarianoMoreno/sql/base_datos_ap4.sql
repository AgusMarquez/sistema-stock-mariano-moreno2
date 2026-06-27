-- ============================================================
-- BASE DE DATOS: Sistema de Gestión de Stock y Órdenes de Compra
-- Institución: Escuela de Chefs Mariano Moreno
-- Trabajo Práctico AP4 - Seminario de Práctica
-- ============================================================

-- Se crea la base de datos si todavía no existe.
CREATE DATABASE IF NOT EXISTS gestion_stock_mm;

-- Se selecciona la base de datos para trabajar sobre ella.
USE gestion_stock_mm;

-- ============================================================
-- TABLA: ingrediente
-- ============================================================
-- Esta tabla almacena los ingredientes utilizados en las clases prácticas.
--
-- Nueva regla de negocio:
-- - Los ingredientes esenciales tienen stock mínimo obligatorio.
-- - Los ingredientes generales se guardan con stock mínimo 0.
-- - Solo los ingredientes esenciales pueden figurar como bajo stock.
-- ============================================================

CREATE TABLE IF NOT EXISTS ingrediente (
    id_ingrediente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    unidad_medida VARCHAR(30) NOT NULL,
    cantidad_disponible DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock_minimo DECIMAL(10,2) NOT NULL DEFAULT 0,
    categoria VARCHAR(50) NOT NULL,
    tipo_producto VARCHAR(50) NOT NULL,
    motivo_esencial VARCHAR(255)
);

-- ============================================================
-- TABLA: movimiento_stock
-- ============================================================
-- Esta tabla registra los ingresos y egresos de stock.
--
-- Cada movimiento queda asociado a un ingrediente.
-- Esto permite mantener un historial de operaciones realizadas.
-- ============================================================

CREATE TABLE IF NOT EXISTS movimiento_stock (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_ingrediente INT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(255),

    CONSTRAINT fk_movimiento_ingrediente
        FOREIGN KEY (id_ingrediente)
        REFERENCES ingrediente(id_ingrediente)
);

-- ============================================================
-- TABLA: orden_compra
-- ============================================================
-- Esta tabla representa la cabecera de una orden de compra.
--
-- Una orden se genera cuando existen ingredientes esenciales
-- con bajo stock.
-- ============================================================

CREATE TABLE IF NOT EXISTS orden_compra (
    id_orden INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente'
);

-- ============================================================
-- TABLA: detalle_orden_compra
-- ============================================================
-- Esta tabla almacena los ingredientes incluidos en una orden.
--
-- Cada registro indica qué ingrediente se solicita y qué cantidad
-- se recomienda comprar.
-- ============================================================

CREATE TABLE IF NOT EXISTS detalle_orden_compra (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT NOT NULL,
    id_ingrediente INT NOT NULL,
    cantidad_solicitada DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_detalle_orden
        FOREIGN KEY (id_orden)
        REFERENCES orden_compra(id_orden),

    CONSTRAINT fk_detalle_ingrediente
        FOREIGN KEY (id_ingrediente)
        REFERENCES ingrediente(id_ingrediente)
);

-- ============================================================
-- DATOS INICIALES DE PRUEBA
-- ============================================================
-- Se cargan ingredientes iniciales para probar el sistema.
--
-- Importante:
-- - Harina, huevos y levadura se cargan como ingredientes esenciales.
-- - Azúcar y sal se cargan como ingredientes generales.
-- - Los ingredientes generales tienen stock mínimo 0 porque no son críticos.
-- ============================================================

INSERT INTO ingrediente 
(nombre, unidad_medida, cantidad_disponible, stock_minimo, categoria, tipo_producto, motivo_esencial)
VALUES
('Harina 000', 'kg', 20, 5, 'Secos', 'Ingrediente esencial', 'Ingrediente base para masas'),
('Huevos', 'unidad', 60, 24, 'Frescos', 'Ingrediente esencial', 'Ingrediente frecuente en clases prácticas'),
('Azúcar', 'kg', 10, 0, 'Secos', 'Ingrediente general', NULL),
('Sal', 'kg', 5, 0, 'Secos', 'Ingrediente general', NULL),
('Levadura', 'kg', 1, 2, 'Frescos', 'Ingrediente esencial', 'Ingrediente crítico para panificados');