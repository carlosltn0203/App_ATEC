package com.example.etacproyect1.entidades

data class Certificado(
    val usu_id: Int,
    val curso_id: Int,
    val fecha_emision: String,
    val certificado_url: String,
    val estudianteCodigo: String,
    val estudianteCurso: String
)
