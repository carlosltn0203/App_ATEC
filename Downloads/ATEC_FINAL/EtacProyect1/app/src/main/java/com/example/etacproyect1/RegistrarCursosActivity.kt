package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.entidades.RegistroCurso
import com.example.etacproyect1.entidades.RegistroPrograma
import com.example.etacproyect1.servicio.RegistroCursoResponse
import com.example.etacproyect1.servicio.RegistroProgramaResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistrarCursosActivity : AppCompatActivity() {

    private lateinit var regCursoAdmin:EditText
    private lateinit var regProgramaCurso:Spinner
    private lateinit var btnGuardarCurso:Button
    private lateinit var btnCancelarCurso:Button
    private lateinit var nameadminCurso:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cursos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
        cargarProgramasEnSpinner()

        //Recuperar código
        val codigo = intent.getStringExtra("codigo")
        nameadminCurso.text = codigo ?: "Código no encontrado"
    }

    private fun asignarReferencias(){
        regCursoAdmin = findViewById(R.id.regCursoAdmin)
        regProgramaCurso = findViewById(R.id.regProgramaCurso)
        btnGuardarCurso = findViewById(R.id.btnGuardarCurso)
        btnGuardarCurso.setOnClickListener {
            registrarCurso()
        }
        btnCancelarCurso = findViewById(R.id.btnCancelarCurso)
        btnCancelarCurso.setOnClickListener {
            val intent: Intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("codigo", nameadminCurso.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        nameadminCurso = findViewById(R.id.nameadminCurso)
    }

    private fun cargarProgramasEnSpinner() {
        lifecycleScope.launch {
            try {
                // Llamada a la API para obtener los programas de estudio
                val response = RetrofitCliente.webService.obtenerProgramas()
                if (response.isSuccessful) {
                    val programas = response.body()?.listaProgramas ?: emptyList()
                    // Crear lista para el Spinner con los nombres de los programas
                    val nombresProgramas = programas.map { it.nombre_programa }
                    val programasAdapter = ArrayAdapter(
                        this@RegistrarCursosActivity,
                        android.R.layout.simple_spinner_item,
                        nombresProgramas
                    )
                    programasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    regProgramaCurso.adapter = programasAdapter
                } else {
                    mostrarMensaje("ATEC  |  ERROR", "No se pudieron cargar los programas.", false)
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
    }

    private fun registrarCurso() {
        val nombre = regCursoAdmin.text.toString().trim()
        // Supón que el programa es representado por el índice seleccionado en el spinner.
        val programa = regProgramaCurso.selectedItemPosition + 1  // El ID es el índice + 1

        // Validaciones adicionales
        if (nombre.isBlank() || programa == null) {
            mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.", false)
            return
        }

        val registroCurso = RegistroCurso(
            nombre_curso = nombre,
            programa_id = programa
        )

        lifecycleScope.launch {
            try {
                val response: Response<RegistroCursoResponse> = RetrofitCliente.webService.registroCurso(registroCurso)
                if (response.isSuccessful) {
                    mostrarMensaje("ATEC  |  REGISTRO DE CURSO", response.body()?.message ?: "Curso registrado correctamente.", true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> mostrarMensaje("ATEC  |  ERROR", "Datos del curso incorrectos o incompletos: $errorBody", false)
                        409 -> mostrarMensaje("ATEC  |  ERROR", "Curso ya registrado.", false)
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
                    intent.putExtra("codigo", nameadminCurso.text.toString()) // Pasar el código
                    startActivity(intent)
                    finish()
                }
            }
            .create()
            .show()
    }
}