package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Inscripcion
import com.google.gson.annotations.SerializedName

data class InscripcionResponse (
    @SerializedName("listaInscripciones") var listaInscripciones:ArrayList<Inscripcion>
)