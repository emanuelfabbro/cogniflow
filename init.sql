-- Habilitar extensión para generar UUIDs si no existe
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Tabla de Transacciones (El corazón del sistema)
CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id UUID NOT NULL, -- Cambiado de VARCHAR a UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL, -- 19 dígitos, 2 decimales (Estándar financiero)
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(20) NOT NULL, -- PENDING, APPROVED, REJECTED, FRAUD
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Alertas de Fraude (Resultado del análisis)
CREATE TABLE IF NOT EXISTS fraud_alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID NOT NULL,
    reason VARCHAR(255) NOT NULL,
    severity VARCHAR(20) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction
        FOREIGN KEY(transaction_id) 
        REFERENCES transactions(id)
);

-- 3. Índices para Performance (Clave para entrevista Senior)
-- Búsquedas rápidas por cuenta para ver historial de transacciones
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
-- Búsquedas por fecha para reportes o ventanas de tiempo en Kafka Streams
CREATE INDEX idx_transactions_created_at ON transactions(created_at);