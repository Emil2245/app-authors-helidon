-- USE distribuida;
-- TABLE: author

CREATE TABLE author (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(255),
                        version INTEGER
);

-- TABLE: book
CREATE TABLE book (
                      isbn VARCHAR(255) PRIMARY KEY,
                      title VARCHAR(255),
                      price DOUBLE PRECISION,
                      version INTEGER
);

-- TABLE: book_author (N:N)
CREATE TABLE book_author (
                             books_isbn VARCHAR(255),
                             authors_id BIGINT,
                             PRIMARY KEY (books_isbn, authors_id),
                             FOREIGN KEY (books_isbn) REFERENCES book(isbn),
                             FOREIGN KEY (authors_id) REFERENCES author(id)
);

-- TABLE: inventory
CREATE TABLE inventory (
                           book_isbn VARCHAR(255) PRIMARY KEY,
                           sold INTEGER,
                           supplied INTEGER,
                           version INTEGER,
                           FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);

-- TABLE: customer
CREATE TABLE customer (
                          id BIGINT PRIMARY KEY,
                          email VARCHAR(255),
                          name VARCHAR(255),
                          version INTEGER
);

-- TABLE: purchase_order
CREATE TABLE purchase_order (
                                id BIGINT PRIMARY KEY,
                                deliveredon TIMESTAMP,
                                placedon TIMESTAMP,
                                status INTEGER,
                                total INTEGER,
                                customer_id BIGINT,
                                FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- TABLE: line_item
CREATE TABLE line_item (
                           idx INTEGER,
                           order_id BIGINT,
                           book_isbn VARCHAR(255),
                           quantity INTEGER,
                           PRIMARY KEY (idx, order_id),
                           FOREIGN KEY (order_id) REFERENCES purchase_order(id),
                           FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);
------------
-- =========================
-- AUTHOR
-- =========================
INSERT INTO author (id, name, version) VALUES
                                           (1, 'Gabriel García Márquez', 0),
                                           (2, 'J.K. Rowling', 0),
                                           (3, 'George Orwell', 0),
                                           (4, 'Isaac Asimov', 0),
                                           (5, 'J.R.R. Tolkien', 0),
                                           (6, 'Stephen King', 0),
                                           (7, 'Haruki Murakami', 0),
                                           (8, 'Jane Austen', 0),
                                           (9, 'Ernest Hemingway', 0),
                                           (10, 'Franz Kafka', 0);
-- =========================
-- BOOK
-- =========================
INSERT INTO book (isbn, title, price, version) VALUES
                                                   ('ISBN-001', 'Cien Años de Soledad', 25.50, 0),
                                                   ('ISBN-002', 'Harry Potter y la Piedra Filosofal', 20.00, 0),
                                                   ('ISBN-003', '1984', 18.90, 0),
                                                   ('ISBN-004', 'Fundación', 22.30, 0),
                                                   ('ISBN-005', 'El Señor de los Anillos', 35.00, 0),
                                                   ('ISBN-006', 'It', 27.99, 0),
                                                   ('ISBN-007', 'Kafka en la Orilla', 24.00, 0),
                                                   ('ISBN-008', 'Orgullo y Prejuicio', 19.50, 0),
                                                   ('ISBN-009', 'El Viejo y el Mar', 15.00, 0),
                                                   ('ISBN-010', 'La Metamorfosis', 14.20, 0);

-- =========================
-- BOOK_AUTHOR (N:N)
-- =========================
INSERT INTO book_author (books_isbn, authors_id) VALUES
                                                     ('ISBN-001', 1),
                                                     ('ISBN-002', 2),
                                                     ('ISBN-003', 3),
                                                     ('ISBN-004', 4),
                                                     ('ISBN-005', 5),
                                                     ('ISBN-006', 6),
                                                     ('ISBN-007', 7),
                                                     ('ISBN-008', 8),
                                                     ('ISBN-009', 9),
                                                     ('ISBN-010', 10);

-- =========================
-- INVENTORY
-- =========================
INSERT INTO inventory (book_isbn, sold, supplied, version) VALUES
                                                               ('ISBN-001', 120, 200, 0),
                                                               ('ISBN-002', 300, 400, 0),
                                                               ('ISBN-003', 250, 350, 0),
                                                               ('ISBN-004', 180, 300, 0),
                                                               ('ISBN-005', 220, 280, 0),
                                                               ('ISBN-006', 150, 250, 0),
                                                               ('ISBN-007', 90, 200, 0),
                                                               ('ISBN-008', 130, 210, 0),
                                                               ('ISBN-009', 170, 260, 0),
                                                               ('ISBN-010', 200, 300, 0);

-- =========================
-- CUSTOMER
-- =========================
INSERT INTO customer (id, email, name, version) VALUES
                                                    (1, 'juan@mail.com', 'Juan Pérez', 0),
                                                    (2, 'ana@mail.com', 'Ana Gómez', 0),
                                                    (3, 'luis@mail.com', 'Luis Torres', 0),
                                                    (4, 'maria@mail.com', 'María López', 0),
                                                    (5, 'carlos@mail.com', 'Carlos Ruiz', 0),
                                                    (6, 'laura@mail.com', 'Laura Mendoza', 0),
                                                    (7, 'pedro@mail.com', 'Pedro Castillo', 0),
                                                    (8, 'sofia@mail.com', 'Sofía Herrera', 0),
                                                    (9, 'diego@mail.com', 'Diego Morales', 0),
                                                    (10, 'valeria@mail.com', 'Valeria Ríos', 0);

-- =========================
-- PURCHASE_ORDER
-- =========================
INSERT INTO purchase_order (id, deliveredon, placedon, status, total, customer_id) VALUES
                                                                                       (1, '2025-01-05 10:00:00', '2025-01-02 09:30:00', 1, 50, 1),
                                                                                       (2, '2025-01-06 11:00:00', '2025-01-03 10:15:00', 1, 40, 2),
                                                                                       (3, '2025-01-07 12:00:00', '2025-01-04 11:00:00', 1, 38, 3),
                                                                                       (4, '2025-01-08 13:00:00', '2025-01-05 12:45:00', 1, 45, 4),
                                                                                       (5, '2025-01-09 14:00:00', '2025-01-06 14:10:00', 1, 70, 5),
                                                                                       (6, '2025-01-10 15:00:00', '2025-01-07 15:30:00', 1, 28, 6),
                                                                                       (7, '2025-01-11 16:00:00', '2025-01-08 16:20:00', 1, 24, 7),
                                                                                       (8, '2025-01-12 17:00:00', '2025-01-09 17:10:00', 1, 39, 8),
                                                                                       (9, '2025-01-13 18:00:00', '2025-01-10 18:40:00', 1, 15, 9),
                                                                                       (10,'2025-01-14 19:00:00', '2025-01-11 19:25:00', 1, 14, 10);

-- =========================
-- LINE_ITEM
-- =========================
INSERT INTO line_item (idx, order_id, book_isbn, quantity) VALUES
                                                               (1, 1, 'ISBN-001', 2),
                                                               (1, 2, 'ISBN-002', 2),
                                                               (1, 3, 'ISBN-003', 2),
                                                               (1, 4, 'ISBN-004', 2),
                                                               (1, 5, 'ISBN-005', 2),
                                                               (1, 6, 'ISBN-006', 1),
                                                               (1, 7, 'ISBN-007', 1),
                                                               (1, 8, 'ISBN-008', 2),
                                                               (1, 9, 'ISBN-009', 1),
                                                               (1, 10,'ISBN-010', 1);

