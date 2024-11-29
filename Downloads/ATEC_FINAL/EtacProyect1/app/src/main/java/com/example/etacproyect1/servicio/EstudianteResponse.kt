package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Estudiante
import com.example.etacproyect1.entidades.ListaEstudianteDoc
import com.google.gson.annotations.SerializedName

data class EstudianteResponse(
    @SerializedName("listaEstudiantes") var listaEstudiantes:ArrayList<Estudiante>
)