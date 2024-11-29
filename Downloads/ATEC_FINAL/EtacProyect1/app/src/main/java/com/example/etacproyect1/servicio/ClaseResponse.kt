package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Clase
import com.google.gson.annotations.SerializedName

data class ClaseResponse (
    @SerializedName("listaClases") var listaClases:ArrayList<Clase>
)