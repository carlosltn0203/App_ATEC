package com.example.etacproyect1.servicio

import com.example.etacproyect1.entidades.Certificado
import com.google.gson.annotations.SerializedName

data class CertificadoResponse (
    @SerializedName("listaCertificados") var listaCertificados:ArrayList<Certificado>
)