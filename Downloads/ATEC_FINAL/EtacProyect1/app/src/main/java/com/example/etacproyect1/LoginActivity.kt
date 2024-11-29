package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Intents
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.databinding.ActivityLoginBinding
import com.example.etacproyect1.entidades.LoginUsuario
import com.example.etacproyect1.servicio.LoginResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var txtcodigo: EditText
    private lateinit var txtclave: EditText
    private lateinit var btnlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
    }

    private fun asignarReferencias(){
        txtcodigo = findViewById(R.id.txtcodigo)
        txtclave = findViewById(R.id.txtclave)
        btnlogin = findViewById(R.id.btnlogin)
        btnlogin.setOnClickListener {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {
        val codigo = txtcodigo.text.toString()
        val clave = txtclave.text.toString()

        if (codigo.isBlank() || clave.isBlank()) {
            mostrarMensaje("ATEC  |  ERROR", "Código ATEC y/o clave incorrecta. Asegúrese de verificar los datos.")
            return
        }

        // Crear objeto con los datos de login
        val loginUsuario = LoginUsuario(
            usu_codigo = codigo,
            usu_clave = clave
        )

        // Llamada a la API usando Retrofit y corrutinas
        lifecycleScope.launch {
            try {
                // Realizar la solicitud al servidor
                val response: retrofit2.Response<LoginResponse> = RetrofitCliente.webService.loginUsuario(loginUsuario)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Obtener el rol del usuario desde la respuesta
                        val userRole = loginResponse.rol_id // Suponiendo que LoginResponse tiene un campo rol_id

                        // Verificar el rol y redirigir a la actividad correspondiente
                        val intent: Intent
                        when (userRole) {
                            1 -> { // Si el rol es 1 (Admin)
                                intent = Intent(this@LoginActivity, AdminActivity::class.java)
                            }
                            2 -> { // Si el rol es 2 (Docente)
                                intent = Intent(this@LoginActivity, DocenteActivity::class.java)
                            }
                            3 -> { // Si el rol es 3 (Usuario)
                                intent = Intent(this@LoginActivity, EstudianteActivity::class.java)
                            }
                            else -> {
                                mostrarMensaje("ATEC  |  ERROR", "Rol desconocido.")
                                return@launch
                            }
                        }

                        // Pasar el rol al siguiente Activity (si lo necesitas)
                        intent.putExtra("USER_ROLE", userRole) // Aquí pasas el rol
                        intent.putExtra("codigo", codigo)

                        // Iniciar la actividad
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarMensaje("ATEC  |  ERROR", "Respuesta vacía del servidor.")
                    }
                } else {
                    // Manejo de errores basado en el código de respuesta HTTP
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.")
                        404 -> mostrarMensaje("ATEC  |  ERROR", "Usuario no encontrado, regístrese por favor.")
                        401 -> mostrarMensaje("ATEC  |  ERROR", "Datos incorrectos, asegúrese de colocar datos válidos.")
                        else -> mostrarMensaje("ATEC  |  ERROR", "Error: ${response.code()} - $errorBody")
                    }
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error: ${e.message}")
            }
        }
    }



    private fun mostrarMensaje(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("ACEPTAR") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}