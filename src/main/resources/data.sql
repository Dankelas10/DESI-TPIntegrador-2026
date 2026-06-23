INSERT INTO ciudad (id, nombre, codigo_postal, eliminado) VALUES
(1, 'Santa Fe', '3000', false);

INSERT INTO persona (id, nombre, apellido, dni, email, telefono, eliminado) VALUES
(1, 'Juan', 'Perez', '30111222', 'juan@email.com', '342111111', false),
(2, 'Maria', 'Gomez', '35222333', 'maria@email.com', '342222222', false);

INSERT INTO propiedad (
    id, direccion, ciudad_id, tipo_propiedad, cantidad_ambientes,
    metros_cuadrados, descripcion, estado, propietario_id, eliminado
) VALUES
(1, 'San Martin 1234', 1, 'CASA', 3, 80, 'Casa céntrica', 'DISPONIBLE', 1, false),
(2, 'Belgrano 456', 1, 'DEPARTAMENTO', 2, 45, 'Departamento luminoso', 'DISPONIBLE', 1, false);