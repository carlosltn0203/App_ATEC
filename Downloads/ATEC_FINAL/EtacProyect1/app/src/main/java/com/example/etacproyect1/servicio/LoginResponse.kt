package com.example.etacproyect1.servicio

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    val message: String,
    val rol_id: Int,
)