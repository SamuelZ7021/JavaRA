CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    race VARCHAR(100),
    age INTEGER NOT NULL,
    owner_name VARCHAR(150) NOT NULL,
    owner_document VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Insertar datos de prueba para que tus citas funcionen (ID 1)
INSERT INTO pets (name, species, race, age, owner_name, owner_document, status)
VALUES ('Firulais', 'PERRO', 'Labrador', 5, 'Juan Perez', '12345678', 'ACTIVA');

ALTER TABLE pets ADD COLUMN owner_id BIGINT;
-- Asignar dueño por defecto (Admin id 1) para no romper datos existentes
UPDATE pets SET owner_id = 1 WHERE owner_id IS NULL;
ALTER TABLE pets ALTER COLUMN owner_id SET NOT NULL;