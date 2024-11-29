

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS db_atec;
USE db_atec;
-- Crear la tabla Usuarios
CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Codigo VARCHAR(20) UNIQUE NOT NULL,
    Clave VARCHAR(255) NOT NULL,
    Cargo varchar(255) NOT NULL
);

-- Crear la tabla Clases
CREATE TABLE Clases (
    idClase INT AUTO_INCREMENT PRIMARY KEY,
    NombreClase VARCHAR(100) NOT NULL,
    idProfesor INT,
    Horario TIME NOT NULL,
    DiaSemana varchar(255) NOT NULL,
    FOREIGN KEY (idProfesor) REFERENCES Usuarios(idUsuario)
);

-- Crear la tabla Asistencias
CREATE TABLE Asistencias (
    idAsistencia INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT,
    idClase INT,
    Fecha DATE NOT NULL,
    Asistido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
    FOREIGN KEY (idClase) REFERENCES Clases(idClase)
);
