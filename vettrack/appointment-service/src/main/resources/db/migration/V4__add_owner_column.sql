ALTER TABLE appointments ADD COLUMN owner_id BIGINT;
-- Asignamos un owner por defecto (el admin, id 1) a las citas existentes para no romper not null
UPDATE appointments SET owner_id = 1 WHERE owner_id IS NULL;
ALTER TABLE appointments ALTER COLUMN owner_id SET NOT NULL;