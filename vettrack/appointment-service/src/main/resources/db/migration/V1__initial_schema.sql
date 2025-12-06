-- 1. Tabla de Veterinarios
CREATE TABLE veterinarians (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Tabla de Citas (Appointments)
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    cancellation_reason VARCHAR(255),
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    CONSTRAINT fk_appointments_veterinarian FOREIGN KEY (veterinarian_id) REFERENCES veterinarians(id)
);

CREATE INDEX idx_appointments_vet_date ON appointments(veterinarian_id, date_time);
