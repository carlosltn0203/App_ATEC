package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.entidades.RegistroPrograma
import com.example.etacproyect1.entidades.RegistroUsuario
import com.example.etacproyect1.servicio.RegistroProgramaResponse
import com.example.etacproyect1.servicio.RegistroResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistrarProgramaEstudio : AppCompatActivity() {

    private lateinit var regProgramaEstudio:EditText
    private lateinit var btnGuardarPrograma:Button
    private lateinit var btnCancelarPrograma:Button
    private lateinit var nameadminPrograma:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_programa_estudio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()

        //Recuperar código
        val codigo = intent.getStringExtra("codigo")
        nameadminPrograma.text = codigo ?: "Código no encontrado"
    }

    private fun asignarReferencias(){
        regProgramaEstudio = findViewById(R.id.regProgramaEstudio)
        btnGuardarPrograma = findViewById(R.id.btnGuardarPrograma)
        btnGuardarPrograma.setOnClickListener {
            registrarPrograma()
        }
        btnCancelarPrograma = findViewById(R.id.btnCancelarPrograma)
        btnCancelarPrograma.setOnClickListener {
            val intent: Intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("codigo", nameadminPrograma.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        nameadminPrograma = findViewById(R.id.nameadminPrograma)
    }

    private fun registrarPrograma() {
        val nombre = regProgramaEstudio.text.toString().trim()

        // Validaciones adicionales
        if (nombre.isBlank()) {
            mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.", false)
            return
        }

        val registroPrograma = RegistroPrograma(
            nombre_programa = nombre
        )

        lifecycleScope.launch {
            try {
                val response: Response<RegistroProgramaResponse> = RetrofitCliente.webService.registroPrograma(registroPrograma)
                if (response.isSuccessful) {
                    mostrarMensaje("ATEC  |  REGISTRO DE PROGRAMA", response.body()?.message ?: "Programa de Estudio registrado correctamente.", true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> mostrarMensaje("ATEC  |  ERROR", "Datos del Programa de Estudio incorrectos o incompletos: $errorBody", false)
                        409 -> mostrarMensaje("ATEC  |  ERROR", "Programa de Estudio ya registrado.", false)
                        else -> mostrarMensaje("ATEC  |  ERROR", "Registro fallido: ${response.code()} - $errorBody", false)
                    }
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
    }

    private fun mostrarMensaje(titulo: String, mensaje: String, navegarALogin: Boolean) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("ACEPTAR") { dialog, _ ->
                dialog.dismiss()
                if (navegarALogin) {
                    // Redirigir a la pantalla de Login
                    val intent = Intent(this, AdminActivity::class.java)
                    intent.putExtra("codigo", nameadminPrograma.text.toString()) // Pasar el código
                    startActivity(intent)
                    finish()
                }
            }
            .create()
            .show()
    }
}