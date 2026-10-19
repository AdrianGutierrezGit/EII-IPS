INSERT INTO persona (id, nombre, apellidos, email, telefono, nif) VALUES
(1, 'Claudio', 'García López', 'claudio.garcia@academia.com', '601111111', '11111111A'),
(2, 'Raquel', 'García López', 'raquel.garcia@academia.com', '601111112', '11111112A'),
(3, 'Alicia', 'García López', 'alicia.garcia@academia.com', '601111113', '11111113A'),
(4, 'Juan', 'García López', 'juan.garcia@academia.com', '601111114', '11111114A'),
(5, 'Rosa', 'García López', 'rosa.garcia@academia.com', '601111115', '11111115A'),
(6, 'Modesto', 'García López', 'pepe.garcia@academia.com', '601111116', '11111116A'),
(7, 'Marta', 'García López', 'marta.garcia@academia.com', '601116113', '11161113A'),
(8, 'Fanjul', 'García López', 'fanjul.garcia@academia.com', '601111614', '11111164A'),
(9, 'Mateo', 'García López', 'mateo.garcia@academia.com', '608111614', '11511164A');



INSERT INTO secretario_administrativo (id_secretario_administrativo) VALUES (5);

INSERT INTO profesor (id_profesor) VALUES (1);
INSERT INTO profesor (id_profesor) VALUES (2);

INSERT INTO responsable_formacion (id_responsable_formacion) VALUES (6);

INSERT INTO alumno (id_alumno) VALUES (3);
INSERT INTO alumno (id_alumno) VALUES (4);
INSERT INTO alumno (id_alumno) VALUES (7);
INSERT INTO alumno (id_alumno) VALUES (8);
INSERT INTO alumno (id_alumno) VALUES (9);

INSERT INTO curso (id_curso, nombre, inicio_plazo_inscripcion, fin_plazo_inscripcion, fecha_inicio, fecha_fin, lugar, num_plazas, estado_curso, objetivos, contenidos) VALUES
(1, 'Nuevas técnicas de Prueba', '2025-07-01', '2025-07-31', '2025-09-01', '2025-09-03', 'Online', 2, 'periodo_inscripcion', 'Aprender lo básico', 'Clases, POO'),
(2, 'Administración de BBDD Neo4J', '2025-12-01', '2025-12-21', '2025-12-26', '2025-12-30', 'Online', 5, 'periodo_inscripcion', 'Aprender lo básico', 'Clases, POO');

INSERT INTO tipo_cuota (id_tipo_cuota, nombre) VALUES (2, 'Normal');
INSERT INTO tipo_cuota (id_tipo_cuota, nombre) VALUES (1, 'Colegiados');


INSERT INTO cuota_curso (id_curso, id_tipo_cuota, precio) VALUES (1, 2, 400);
INSERT INTO cuota_curso (id_curso, id_tipo_cuota, precio) VALUES (1, 1, 200);
INSERT INTO cuota_curso (id_curso, id_tipo_cuota, precio) VALUES (2, 2, 400);
INSERT INTO cuota_curso (id_curso, id_tipo_cuota, precio) VALUES (2, 1, 200);

INSERT INTO inscripcion (fecha_solicitud,id_curso,id_alumno,id_cuota,estado,cantidad_pagada,balance_inscripcion) VALUES
('2025-07-15',1,3,2,'PAGADO',200,0),
('2025-07-20',1,4,1,'PAGADO',400,0),
('2025-12-01',2,7,3,'PENDIENTE',300,-100),
('2025-12-01',2,8,4,'PAGADO',200,0),
('2025-12-02',2,9,4,'PENDIENTE',0,-200);

INSERT INTO pago (cantidad_pagada,fecha_pago,id_inscripcion) values
(200,'2025-07-16',1);
INSERT INTO pago (cantidad_pagada,fecha_pago,id_inscripcion) values
(400,'2025-07-20',2);
INSERT INTO pago (cantidad_pagada,fecha_pago,id_inscripcion) values
(300,'2025-12-02',3);
INSERT INTO pago (cantidad_pagada,fecha_pago,id_inscripcion) values
(100,'2025-12-01',4);
INSERT INTO pago (cantidad_pagada,fecha_pago,id_inscripcion) values
(100,'2025-12-02',4);

INSERT INTO actividad_profesor (id_curso, profesor_id, remuneracion) VALUES (1, 1, 250);
INSERT INTO actividad_profesor (id_curso, profesor_id, remuneracion) VALUES (1, 2, 250);
INSERT INTO actividad_profesor (id_curso, profesor_id, remuneracion) VALUES (2, 2, 750);

insert into factura_profesor (id_factura,fecha_emision,cantidad_facturada,nombre_emisor,direccion_emisor,estado_factura,id_actividad_profesor) values
(1,'2025-09-04',250,'Claudio','claudio.garcia@academia.com','PAGADO',1);

insert into pago_profesor(id_pago_profesor,fecha_transferencia,cantidad_transferida,id_factura) values
(1,'2025-09-05',250,1);