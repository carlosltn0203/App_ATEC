package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Programa
import com.google.gson.annotations.SerializedName

data class ProgramaResponse(
    @SerializedName("listaProgramas") var listaProgramas:ArrayList<Programa>
)