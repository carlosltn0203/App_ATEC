package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.ListaEstudianteDoc
import com.google.gson.annotations.SerializedName

data class ListaEstudianteResponse (
    @SerializedName("listaEstudiantesDoc") var listaEstudiantesDoc:ArrayList<ListaEstudianteDoc>
)