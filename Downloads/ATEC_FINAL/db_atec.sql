-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema db_atec
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema db_atec
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_atec` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `db_atec` ;

-- -----------------------------------------------------
-- Table `db_atec`.`programas_estudio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`programas_estudio` (
  `programa_id` INT NOT NULL AUTO_INCREMENT,
  `nombre_programa` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`programa_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`cursos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`cursos` (
  `curso_id` INT NOT NULL AUTO_INCREMENT,
  `nombre_curso` VARCHAR(255) NOT NULL,
  `programa_id` INT NOT NULL,
  PRIMARY KEY (`curso_id`),
  INDEX `fk_cursos_programas_idx` (`programa_id` ASC) VISIBLE,
  CONSTRAINT `fk_cursos_programas`
    FOREIGN KEY (`programa_id`)
    REFERENCES `db_atec`.`programas_estudio` (`programa_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`roles` (
  `rol_id` INT NOT NULL AUTO_INCREMENT,
  `rol_nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`rol_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`usuarios` (
  `usu_id` INT NOT NULL AUTO_INCREMENT,
  `usu_dni` INT NOT NULL,
  `usu_nombres` VARCHAR(45) NOT NULL,
  `usu_apellidos` VARCHAR(45) NOT NULL,
  `usu_codigo` VARCHAR(45) NOT NULL,
  `usu_clave` VARCHAR(255) NOT NULL,
  `usu_correo` VARCHAR(45) NOT NULL,
  `usu_sexo` VARCHAR(45) NOT NULL,
  `usu_telefono` VARCHAR(45) NOT NULL,
  `rol_id` INT NOT NULL,
  `programa_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`usu_id`),
  INDEX `fk_usuarios_roles_idx` (`rol_id` ASC) VISIBLE,
  INDEX `fk_usuarios_programa_idx` (`programa_id` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_programa`
    FOREIGN KEY (`programa_id`)
    REFERENCES `db_atec`.`programas_estudio` (`programa_id`),
  CONSTRAINT `fk_usuarios_roles`
    FOREIGN KEY (`rol_id`)
    REFERENCES `db_atec`.`roles` (`rol_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`certificados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`certificados` (
  `certificado_id` INT NOT NULL AUTO_INCREMENT,
  `usu_id` INT NOT NULL,
  `curso_id` INT NOT NULL,
  `fecha_emision` VARCHAR(45) NOT NULL,
  `certificado_url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`certificado_id`),
  INDEX `fk_certificados_usuarios_idx` (`usu_id` ASC) VISIBLE,
  INDEX `fk_certificados_cursos_idx` (`curso_id` ASC) VISIBLE,
  CONSTRAINT `fk_certificados_cursos`
    FOREIGN KEY (`curso_id`)
    REFERENCES `db_atec`.`cursos` (`curso_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_certificados_usuarios`
    FOREIGN KEY (`usu_id`)
    REFERENCES `db_atec`.`usuarios` (`usu_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`horarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`horarios` (
  `horario_id` INT NOT NULL AUTO_INCREMENT,
  `curso_id` INT NOT NULL,
  `dia` VARCHAR(20) NOT NULL,
  `hora_inicio` VARCHAR(45) NOT NULL,
  `hora_fin` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`horario_id`),
  INDEX `fk_horarios_cursos_idx` (`curso_id` ASC) VISIBLE,
  CONSTRAINT `fk_horarios_cursos`
    FOREIGN KEY (`curso_id`)
    REFERENCES `db_atec`.`cursos` (`curso_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `db_atec`.`inscripciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_atec`.`inscripciones` (
  `inscripcion_id` INT NOT NULL AUTO_INCREMENT,
  `usu_id` INT NOT NULL,
  `programa_id` INT NOT NULL,
  `estado` ENUM('aprobado', 'desaprobado') NOT NULL,
  PRIMARY KEY (`inscripcion_id`),
  INDEX `fk_inscripciones_usuarios_idx` (`usu_id` ASC) VISIBLE,
  INDEX `fk_inscripciones_programas_estudio1_idx` (`programa_id` ASC) VISIBLE,
  CONSTRAINT `fk_inscripciones_programas_estudio1`
    FOREIGN KEY (`programa_id`)
    REFERENCES `db_atec`.`programas_estudio` (`programa_id`),
  CONSTRAINT `fk_inscripciones_usuarios`
    FOREIGN KEY (`usu_id`)
    REFERENCES `db_atec`.`usuarios` (`usu_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

