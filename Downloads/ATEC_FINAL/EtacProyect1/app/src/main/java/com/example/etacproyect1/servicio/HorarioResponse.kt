package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Horario
import com.google.gson.annotations.SerializedName

data class HorarioResponse (
    @SerializedName("listaHorarios") var listaHorarios:ArrayList<Horario>
)