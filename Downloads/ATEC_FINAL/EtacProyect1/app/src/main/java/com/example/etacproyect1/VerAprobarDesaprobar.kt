package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.entidades.RegistroCurso
import com.example.etacproyect1.entidades.RegistroInscripcion
import com.example.etacproyect1.servicio.RegistroCursoResponse
import com.example.etacproyect1.servicio.RegistroInscripcionResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.launch
import retrofit2.Response

class VerAprobarDesaprobar : AppCompatActivity() {

    private lateinit var regNombresInscrip:Spinner
    private lateinit var regProgramasInscrip:Spinner
    private lateinit var estadoAprobar: CheckBox
    private lateinit var estadoDesaprobar: CheckBox
    private lateinit var btnGuardarInscrip: Button
    private lateinit var btnCancelarInscrip: Button
    private lateinit var btnCerrarSesionInscrip: ImageView
    private lateinit var namedocenteInscrip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_aprobar_desaprobar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
        cargarNombresEnSpinner()
        cargarProgramasEnSpinner()

        //Recuperar código
        val codigo = intent.getStringExtra("codigo")
        namedocenteInscrip.text = codigo ?: "Código no encontrado"
    }

    private fun asignarReferencias(){
        regNombresInscrip = findViewById(R.id.regNombresInscrip)
        regProgramasInscrip = findViewById(R.id.regProgramasInscrip)
        estadoAprobar = findViewById(R.id.estadoAprobar)
        estadoDesaprobar = findViewById(R.id.estadoDesaprobar)
        btnGuardarInscrip = findViewById(R.id.btnGuardarInscrip)
        namedocenteInscrip = findViewById(R.id.namedocenteInscrip)

        btnGuardarInscrip.setOnClickListener {
            registrarInscripcion()
        }
        btnCancelarInscrip = findViewById(R.id.btnCancelarInscrip)
        btnCancelarInscrip.setOnClickListener {
            val intent: Intent = Intent(this, DocenteActivity::class.java)
            intent.putExtra("codigo", namedocenteInscrip.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionInscrip = findViewById(R.id.btnCerrarSesionInscrip)
        btnCerrarSesionInscrip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarNombresEnSpinner() {
        lifecycleScope.launch {
            try {
                // Llamada a la API para obtener los alumnos
                val response = RetrofitCliente.webService.obtenerEstudiantes() // Asegúrate de tener este endpoint
                if (response.isSuccessful) {
                    val estudiantes = response.body()?.listaEstudiantes ?: emptyList()

                    // Crear lista con los nombres completos (nombre + apellido)
                    val nombresCompletos = estudiantes.map { "${it.usu_nombres} ${it.usu_apellidos}" }

                    // Crear el adaptador para el Spinner
                    val estudiantesAdapter = ArrayAdapter(
                        this@VerAprobarDesaprobar,
                        android.R.layout.simple_spinner_item,
                        nombresCompletos
                    )
                    estudiantesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    regNombresInscrip.adapter = estudiantesAdapter
                } else {
                    mostrarMensaje("ATEC  |  ERROR", "No se pudieron cargar los estudiantes", false)
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
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
                        this@VerAprobarDesaprobar,
                        android.R.layout.simple_spinner_item,
                        nombresProgramas
                    )
                    programasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    regProgramasInscrip.adapter = programasAdapter
                } else {
                    mostrarMensaje("ATEC  |  ERROR", "No se pudieron cargar los Programas de Estudio.", false)
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
    }

    private fun registrarInscripcion() {
        val nombre = regNombresInscrip.selectedItemPosition + 1  // El ID es el índice + 1
        val programa = regProgramasInscrip.selectedItemPosition + 1  // El ID es el índice + 1
        val estado = when {
            estadoAprobar.isChecked -> "aprobado"
            estadoDesaprobar.isChecked -> "desaprobado"
            else -> ""  // Si ninguno de los dos está marcado, podrías manejarlo como un error o dejarlo vacío
        }

        estadoAprobar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                estadoDesaprobar.isChecked = false  // Desmarcar el otro CheckBox
            }
        }

        estadoDesaprobar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                estadoAprobar.isChecked = false  // Desmarcar el otro CheckBox
            }
        }

        // Validaciones adicionales
        if (nombre == null || programa == null) {
            mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.", false)
            return
        }

        val registroInscripcion = RegistroInscripcion(
            usu_id = nombre,
            programa_id = programa,
            estado = estado

        )

        lifecycleScope.launch {
            try {
                val response: Response<RegistroInscripcionResponse> = RetrofitCliente.webService.registroInscripcion(registroInscripcion)
                if (response.isSuccessful) {
                    mostrarMensaje("ATEC  |  INSCRIPCIÓN DE NOTAS", response.body()?.message ?: "Estudiante calificado correctamente.", true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> mostrarMensaje("ATEC  |  ERROR", "Este estudiante ya fue calificado.: $errorBody", false)
                        409 -> mostrarMensaje("ATEC  |  ERROR", "Este estudiante ya está calificado en este Programa de Estudio.", false)
                        else -> mostrarMensaje("ATEC  |  ERROR", "Calificación fallida: ${response.code()} - $errorBody", false)
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
                    val intent = Intent(this, DocenteActivity::class.java)
                    intent.putExtra("codigo", namedocenteInscrip.text.toString()) // Pasar el código
                    startActivity(intent)
                    finish()
                }
            }
            .create()
            .show()
    }
}