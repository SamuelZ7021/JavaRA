-- Ejemplo: Si necesitamos una columna de estado para el filtrado dinámico mencionado en la HU
ALTER TABLE events ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';