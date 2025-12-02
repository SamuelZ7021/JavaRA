-- Agregar Foreign Key
ALTER TABLE events
ADD CONSTRAINT fk_event_venue
FOREIGN KEY (venue_id) REFERENCES venues(id)
ON DELETE CASCADE;

-- Indices para mejorar performance (Requerimiento de optimización)
CREATE INDEX idx_event_venue ON events(venue_id);
CREATE INDEX idx_event_date ON events(start_date);
CREATE INDEX idx_venue_city ON venues(city);