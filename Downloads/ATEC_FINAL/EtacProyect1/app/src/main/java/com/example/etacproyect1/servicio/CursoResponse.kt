package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Curso
import com.google.gson.annotations.SerializedName

data class CursoResponse (
    @SerializedName("listaCursos") var listaCursos:ArrayList<Curso>
)