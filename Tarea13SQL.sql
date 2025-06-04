
-- Tabla de categorías
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de libros
CREATE TABLE libros (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    categoria_id INTEGER REFERENCES categorias(id),
    disponible BOOLEAN DEFAULT TRUE,
    fecha_publicacion DATE
);

-- Tabla de préstamos
CREATE TABLE prestamos (
    id SERIAL PRIMARY KEY,
    libro_id INTEGER REFERENCES libros(id),
    usuario_id INTEGER REFERENCES usuarios(id),
    fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion_prevista TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL '15 days'),
    fecha_devolucion_real TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'COMPLETADO', 'VENCIDO'))
);

-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Ficción', 'Libros de narrativa imaginativa.'),
('Historia', 'Obras sobre hechos históricos.'),
('Tecnología', 'Libros sobre avances tecnológicos.'),
('Ciencia', 'Obras científicas y divulgación.'),
('Arte', 'Libros sobre arte y creatividad.');

-- Insertar usuarios
INSERT INTO usuarios (nombre, email) VALUES
('Juan Pérez', 'juan.perez@example.com'),
('Ana Gómez', 'ana.gomez@example.com'),
('Carlos López', 'carlos.lopez@example.com'),
('Luisa Fernández', 'luisa.fernandez@example.com'),
('Pedro Ramírez', 'pedro.ramirez@example.com');

-- Insertar libros
INSERT INTO libros (titulo, autor, isbn, categoria_id, disponible, fecha_publicacion) VALUES
('1984', 'George Orwell', '9780451524935', 1, TRUE, '1949-06-08'),
('Breve historia del tiempo', 'Stephen Hawking', '9780553380163', 4, TRUE, '1988-04-01'),
('El arte de la guerra', 'Sun Tzu', '9788483233163', 2, TRUE, '500-01-01'),
('El código Da Vinci', 'Dan Brown', '9780307474278', 1, TRUE, '2003-03-18'),
('El gen egoísta', 'Richard Dawkins', '9780198788607', 4, TRUE, '1976-01-01'),
('Clean Code', 'Robert C. Martin', '9780132350884', 3, TRUE, '2008-08-01'),
('Historia del arte', 'Ernst Gombrich', '9780714832470', 5, TRUE, '1950-01-01'),
('Cien años de soledad', 'Gabriel García Márquez', '9780307474728', 1, TRUE, '1967-05-30');

-- Índices para mejorar el rendimiento
CREATE INDEX idx_libros_categoria ON libros(categoria_id);
CREATE INDEX idx_prestamos_usuario ON prestamos(usuario_id);
CREATE INDEX idx_prestamos_libro ON prestamos(libro_id);