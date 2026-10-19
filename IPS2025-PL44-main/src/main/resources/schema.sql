-- Borrar tablas en orden correcto (de detalle a maestro)
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS inscripcion;
DROP TABLE IF EXISTS actividad_profesor;
DROP TABLE IF EXISTS curso;
DROP TABLE IF EXISTS profesor;
DROP TABLE IF EXISTS alumno;
DROP TABLE IF EXISTS responsable_formacion;
DROP TABLE IF EXISTS persona;
DROP TABLE IF EXISTS pago_profesor;
DROP TABLE IF EXISTS factura_profesor;
DROP TABLE IF EXISTS secretario_administrativo;
DROP TABLE IF EXISTS tipo_cuota;
DROP TABLE IF EXISTS cuota_curso;
DROP TABLE IF EXISTS empresa;
CREATE TABLE persona
(
   id INT PRIMARY KEY,
   nombre VARCHAR (100) NOT NULL,
   apellidos VARCHAR (100) NOT NULL,
   email VARCHAR (100) NOT NULL,
   telefono VARCHAR (100),
   nif VARCHAR (100)
);
-- Catálogo de tipos de cuota
CREATE TABLE tipo_cuota
(
   id_tipo_cuota INT PRIMARY KEY,
   nombre VARCHAR(100) NOT NULL UNIQUE
);
-- Precio asignado a cada tipo de cuota en un curso
CREATE TABLE cuota_curso
(
   id_cuota_curso INTEGER PRIMARY KEY AUTOINCREMENT,
   id_curso INT NOT NULL,
   id_tipo_cuota INT NOT NULL,
   precio INT NOT NULL CHECK
   (
      precio >= 0
   ),
   UNIQUE
   (
      id_curso,
      id_tipo_cuota
   ),
   FOREIGN KEY (id_curso) REFERENCES curso (id_curso),
   FOREIGN KEY (id_tipo_cuota) REFERENCES tipo_cuota (id_tipo_cuota)
);
-- Tabla secretario_administrativo
CREATE TABLE secretario_administrativo
(
   id_secretario_administrativo INTEGER PRIMARY KEY AUTOINCREMENT,
   FOREIGN KEY (id_secretario_administrativo) REFERENCES persona (id)
);
CREATE TABLE profesor
(
   id_profesor INTEGER PRIMARY KEY AUTOINCREMENT,
   FOREIGN KEY (id_profesor) REFERENCES persona (id)
);
-- Tabla responsable_formacion
CREATE TABLE responsable_formacion
(
   id_responsable_formacion INTEGER PRIMARY KEY AUTOINCREMENT,
   FOREIGN KEY (id_responsable_formacion) REFERENCES persona (id)
);
-- Tabla alumno
CREATE TABLE alumno
(
   id_alumno INTEGER PRIMARY KEY AUTOINCREMENT,
   FOREIGN KEY (id_alumno) REFERENCES persona (id)
);
CREATE TABLE curso
(
   id_curso INTEGER PRIMARY KEY,
   nombre VARCHAR (100),
   inicio_plazo_inscripcion DATE NOT NULL,
   fin_plazo_inscripcion DATE NOT NULL,
   fecha_inicio DATE NOT null,
   fecha_fin DATE NOT null,
   lugar VARCHAR (100),
   num_plazas INT NOT NULL,
   estado_curso VARCHAR (10) NOT NULL,
   objetivos TEXT,
   contenidos TEXT,
   retrasado BOOLEAN DEFAULT FALSE,
   fecha_retraso DATE DEFAULT NULL,
   id_empresa INTEGER REFERENCES empresa(id_empresa),
   coste_empresa INTEGER DEFAULT 0,
   CHECK
   (
      estado_curso in
      (
         'planificado',
         'periodo_inscripcion',
         'inscripcion_cerrada',
         'en_celebracion',
         'celebrado',
         'curso_cerrado',
         'cancelado'
      )
   ),
   CHECK
   (
      inicio_plazo_inscripcion <= fin_plazo_inscripcion
   )
);
-- Tabla profesor
-- Relación actividad_profesor
CREATE TABLE actividad_profesor
(
   id_actividad_profesor INTEGER PRIMARY KEY AUTOINCREMENT,
   id_curso INT NOT NULL,
   profesor_id INT NOT NULL,
   remuneracion INT NOT NULL CHECK
   (
      remuneracion >= 0
   ),
   FOREIGN KEY (id_curso) REFERENCES curso (id_curso),
   FOREIGN KEY (profesor_id) REFERENCES profesor (id_profesor)
);
-- Tabla inscripcion
CREATE TABLE inscripcion
(
   id_inscripcion INTEGER PRIMARY KEY AUTOINCREMENT,
   fecha_solicitud DATE NOT NULL,
   id_curso INT NOT NULL,
   id_alumno INT NOT NULL,
   id_cuota INT NOT NULL,
   estado VARCHAR (50) NOT NULL,
   cantidad_pagada INT NOT NULL,
   balance_inscripcion INT NOT NULL,
   CHECK
   (
      estado in
      (
         'PAGADO',
         'PENDIENTE',
         'CANCELADO',
         'LISTA_DE_ESPERA'
      )
   ),
   FOREIGN KEY (id_curso) REFERENCES curso (id_curso),
   FOREIGN KEY (id_alumno) REFERENCES alumno (id_alumno),
   FOREIGN KEY (id_cuota) REFERENCES cuota_curso (id_cuota_curso)
);
-- Tabla pago
CREATE TABLE pago
(
   id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
   cantidad_pagada INT NOT NULL,
   fecha_pago DATE NOT NULL,
   id_inscripcion INT NOT NULL,
   FOREIGN KEY (id_inscripcion) REFERENCES inscripcion (id_inscripcion)
);
CREATE TABLE pago_profesor
(
   id_pago_profesor INTEGER PRIMARY KEY AUTOINCREMENT,
   fecha_transferencia DATE NOT NULL,
   cantidad_transferida INT NOT NULL,
   id_factura INT NOT NULL,
   FOREIGN KEY (id_factura) REFERENCES factura_profesor (id_factura)
);
CREATE TABLE factura_profesor
(
   id_factura INTEGER PRIMARY KEY AUTOINCREMENT,
   fecha_emision DATE NOT NULL,
   cantidad_facturada INTEGER NOT NULL CHECK
   (
      cantidad_facturada >= 0
   ),
   nombre_emisor VARCHAR (100) NOT NULL,
   direccion_emisor VARCHAR (200) NOT NULL,
   estado_factura VARCHAR (100) NOT NULL,
   id_actividad_profesor INT NOT NULL,
   CHECK
   (
      estado_factura in
      (
         'PAGADO',
         'PENDIENTE',
         'CONFLICTO'
      )
   ),
   FOREIGN KEY (id_actividad_profesor) REFERENCES actividad_profesor (id_actividad_profesor)
);

-- Nueva tabla
CREATE TABLE empresa (
    id_empresa INTEGER PRIMARY KEY AUTOINCREMENT,
    cif VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL
);

