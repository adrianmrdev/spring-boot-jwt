/* Populate tables */

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (1, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (2, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (3, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (4, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (5, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (6, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (7, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (8, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (9, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (10, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (11, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (12, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (13, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (14, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (15, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (16, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (17, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (18, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (19, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (20, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (21, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (22, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (23, 'Adrián', 'Mosquera', 'adrian@mail.com', '2020-01-01', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (24, 'Carolina', 'Roo', 'carolina@mail.com', '2020-01-01', '');

/* Productos */
INSERT INTO productos (nombre, precio, create_at) VALUES ('Panasonic LCD', 259990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sony camara digital', 456790, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Apple iPad', 987826, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sony Notebook', 134520, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Impresora HP Multifunción', 19990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Bicicleta BH', 79990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Mica Comoda 5 Cajones', 239990, NOW());

/* Facturas */

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura equipos deportivos', 'Alguna observación importante', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3, 2, 6);

