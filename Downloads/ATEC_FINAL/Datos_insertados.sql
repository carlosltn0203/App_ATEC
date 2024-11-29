SELECT * FROM programas_estudio;
INSERT INTO programas_estudio VALUES (NULL, 'Ofimática');
INSERT INTO programas_estudio VALUES (NULL, 'Arquitectura');
INSERT INTO programas_estudio VALUES (NULL, 'Diseño Gráfico');

SELECT * FROM cursos;
INSERT INTO cursos VALUE (NULL, 'Microsoft Word', 1);
INSERT INTO cursos VALUES (NULL, 'Microsoft PowerPoint', 1);
INSERT INTO cursos VALUES (NULL, 'Microsoft Excel', 1);
INSERT INTO cursos VALUES (NULL, 'Microsoft Access', 1);
INSERT INTO cursos VALUES (NULL, 'Microsoft Project', 1);
INSERT INTO cursos VALUES (NULL, 'SketchUp', 2);
INSERT INTO cursos VALUES (NULL, 'VRay', 2);
INSERT INTO cursos VALUES (NULL, 'Lumion', 2);
INSERT INTO cursos VALUES (NULL, 'Photoshop', 3);
INSERT INTO cursos VALUES (NULL, 'Illustrator', 3);
INSERT INTO cursos VALUES (NULL, 'Figma', 3);


SELECT * FROM programas_estudio;
INSERT INTO programas_estudio VALUES (NULL, 'Ofimática');
INSERT INTO programas_estudio VALUES (NULL, 'Arquitectura');
INSERT INTO programas_estudio VALUES (NULL, 'Diseño Gráfico');

SELECT * FROM roles;
INSERT INTO roles VALUE (NULL, 'Administrador');
INSERT INTO roles VALUE (NULL, 'Docente');
INSERT INTO roles VALUE (NULL, 'Estudiante');

SELECT * FROM usuarios;
INSERT INTO usuarios VALUES (NULL, 12345678, 'Álvaro', 'Timana', 'AD0101', 'at12345678', 'AD0101@atec.pe', 'M', '941763247', 1, NULL);
INSERT INTO usuarios VALUES (NULL, 23456789, 'Fabián', 'Ronceros', 'DC0101', 'fr23456789', 'DC0101@atec.pe', 'M', '987561423', 2, 3);
INSERT INTO usuarios VALUES (NULL, 34567891, 'Leandro', 'Sánchez', 'ES0101', 'ls34567891', 'ES0101@atec.pe', 'M', '919637824', 3, 1);
INSERT INTO usuarios VALUES (NULL, 45678910, 'Rodrigo', 'Peña', 'ES0102', 'rp45678910', 'ES0102@atec.pe', 'M', '974186357', 3, 3);
INSERT INTO usuarios VALUES (NULL, 56789101, 'Laura', 'Montero', 'ES0103', 'lm56789101', 'ES0103@atec.pe', 'F', '946821307', 3, 2);

SELECT * FROM certificados;
INSERT INTO certificados VALUES (NULL, 3, 1, '15/01/2024', 'https://certificados.com/word_cert_3.pdf');
INSERT INTO certificados VALUES (NULL, 3, 2, '02/10/2024', 'https://certificados.com/ppt_cert_3.pdf');
INSERT INTO certificados VALUES (NULL, 4, 6, '12/03/2024', 'https://certificados.com/sketchup_cert_4.pdf');
INSERT INTO certificados VALUES (NULL, 4, 8, '22/05/2024', 'https://certificados.com/lumion_cert_4.pdf');
INSERT INTO certificados VALUES (NULL, 3, 3, '18/04/2024', 'https://certificados.com/excel_cert_3.pdf');

SELECT * FROM horarios;
INSERT INTO horarios VALUES (NULL, 1, 'Lunes', '09 AM', '11 AM');
INSERT INTO horarios VALUES (NULL, 1, 'Miércoles', '07 AM', '9 AM');
INSERT INTO horarios VALUES (NULL, 2, 'Martes', '10 AM', '12 PM');
INSERT INTO horarios VALUES (NULL, 3, 'Jueves', '02 PM', '04 PM');
INSERT INTO horarios VALUES (NULL, 4, 'Viernes', '03 PM', '05 PM');
INSERT INTO horarios VALUES (NULL, 5, 'Sábado', '10 AM', '01 PM');
INSERT INTO horarios VALUES (NULL, 6, 'Lunes', '04 PM', '06 PM');
INSERT INTO horarios VALUES (NULL, 7, 'Miércoles', '10 AM', '12 PM');
INSERT INTO horarios VALUES (NULL, 8, 'Viernes', '09 AM', '11 AM');
INSERT INTO horarios VALUES (NULL, 9, 'Martes', '01 PM', '03 PM');
INSERT INTO horarios VALUES (NULL, 10, 'Jueves', '11 AM', '01 PM');
INSERT INTO horarios VALUES (NULL, 11, 'Sábado', '08 AM', '10 AM');

SELECT * FROM inscripciones;
INSERT INTO inscripciones VALUES (NULL, 3, 1, 'aprobado');
INSERT INTO inscripciones VALUES (NULL, 3, 2, 'aprobado');
INSERT INTO inscripciones VALUES (NULL, 3, 3, 'desaprobado');
INSERT INTO inscripciones VALUES (NULL, 4, 6, 'aprobado');
INSERT INTO inscripciones VALUES (NULL, 4, 7, 'desaprobado');
