CREATE TABLE diagnoses (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    treatment VARCHAR(255) NOT NULL,
    recommendations VARCHAR(255) NOT NULL,
    appointment_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_diagnosis_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);