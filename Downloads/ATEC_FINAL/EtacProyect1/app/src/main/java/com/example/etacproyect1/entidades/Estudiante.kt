package com.example.etacproyect1.entidades

data class Estudiante (
    val usu_dni: Int,
    val usu_nombres: String,
    val usu_apellidos: String,
    val usu_correo: String,
    val usu_codigo: String,
    val usu_clave: String,
    val usu_sexo: String,
    val usu_telefono: String,
    val rol_id: Int,
    val programa_id: Int,  // Este objeto debe contener el nombre del programa
    val nombrePrograma: String
)