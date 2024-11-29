package com.example.etacproyect1.entidades

data class Horario(
    val curso_id: Int,
    val dia: String,
    val hora_inicio: String,
    val hora_fin: String,
    val nombreCurso: String // Agrega este campo
)
