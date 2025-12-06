-- V3__initial_data.sql
-- Precarga de datos para pruebas

INSERT INTO veterinarians (name, email, phone, active) VALUES
('Dr. House', 'house@vettrack.com', '555-0101', true),
('Dra. Pol', 'pol@vettrack.com', '555-0102', true),
('Dr. Dolittle', 'dolittle@vettrack.com', '555-0103', false); -- Inactivo para probar validaciones

-- Usuario ADMIN para pruebas de seguridad
-- Password: 'password' (encriptada con BCrypt)
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$wS6zP5.g.XyQ1h.y6.u.EO.u.u.u.u.u.u.u.u.u.u.u.u.u', 'ADMIN');