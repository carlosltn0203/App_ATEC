package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.entidades.RegistroUsuario
import com.example.etacproyect1.entidades.Usuario
import com.example.etacproyect1.servicio.ProgramaResponse
import com.example.etacproyect1.servicio.RegistroResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistrarEstudiante : AppCompatActivity() {
    private lateinit var regDNI:EditText
    private lateinit var regNombres:EditText
    private lateinit var regApellidos:EditText
    private lateinit var regCodigo:EditText
    private lateinit var regClave:EditText
    private lateinit var regCorreo:EditText
    private lateinit var regPrograma:Spinner
    private lateinit var regSexo:Spinner
    private lateinit var regTelefono:EditText
    private lateinit var btnGuardarEstudiante:Button
    private lateinit var btnCancelarEstudiante:Button
    private lateinit var btnCerrarSesionRegistrar:ImageView
    private lateinit var nameadminRegistrar:TextView
    private lateinit var lblTitulo:TextView
    private var editar = false
    private var codigoEstudiante: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_estudiante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()

        //Recibimos código
        val codigo = intent.getStringExtra("codigo")
        nameadminRegistrar.text = codigo ?: "Código no encontrado"

        // Revisar si hay datos enviados para editar un estudiante
        val usuDNI = intent.getStringExtra("usu_dni")?.toIntOrNull()
        val usuNombres = intent.getStringExtra("usu_nombres")
        val usuApellidos = intent.getStringExtra("usu_apellidos")
        val usuCodigo = intent.getStringExtra("usu_codigo")
        val usuClave = intent.getStringExtra("usu_clave")
        val usuCorreo = intent.getStringExtra("usu_correo")
        val programaNombre = intent.getStringExtra("programa_nombre")
        val usuTelefono = intent.getStringExtra("usu_telefono")

        // Si recibimos datos del estudiante, estamos en modo edición
        if (usuCodigo != null) {
            editar = true
            codigoEstudiante = usuCodigo  // Guardamos el código del estudiante que se está editando
            regDNI.setText(usuDNI.toString())
            regNombres.setText(usuNombres)
            regApellidos.setText(usuApellidos)
            regCodigo.setText(usuCodigo)
            regClave.setText(usuClave)
            regCorreo.setText(usuCorreo)
            regTelefono.setText(usuTelefono)

            val usuSexo = intent.getStringExtra("usu_sexo")
            usuSexo?.let {
                val sexoIndex = (regSexo.adapter as ArrayAdapter<String>).getPosition(it)
                regSexo.setSelection(if (sexoIndex != -1) sexoIndex else 0)
            }

            // Cambiar título y texto del botón
            lblTitulo.text = "ACTUALIZACIÓN DE ESTUDIANTE"
            btnGuardarEstudiante.text = "ACTUALIZAR"


            // Hacer que regCodigo y regClave sean no editables y con un estilo gris
            regCodigo.isEnabled = false
            regClave.isEnabled = false

            // Opcional: cambiar el color del texto a gris para indicar que está deshabilitado
            regCodigo.setTextColor(ContextCompat.getColor(this, R.color.gray))
            regClave.setTextColor(ContextCompat.getColor(this, R.color.gray))
        } else {
            // Si no hay datos, es un nuevo registro
            lblTitulo.text = "REGISTRO DE ESTUDIANTE"
            btnGuardarEstudiante.text = "REGISTRAR"
        }

        // Cargar los programas en el Spinner
        cargarProgramas()
    }

    private fun asignarReferencias(){
        regDNI = findViewById(R.id.regDNI)
        regNombres = findViewById(R.id.regNombres)
        regApellidos = findViewById(R.id.regApellidos)
        regCodigo = findViewById(R.id.regCodigo)
        regClave = findViewById(R.id.regClave)
        regCorreo = findViewById(R.id.regCorreo)
        regTelefono = findViewById(R.id.regTelefono)
        lblTitulo = findViewById(R.id.lblTitulo)
        btnCerrarSesionRegistrar = findViewById(R.id.btnCerrarSesionRegistrar)
        nameadminRegistrar = findViewById(R.id.nameadminRegistrar)
        regPrograma = findViewById(R.id.regPrograma)
        btnCerrarSesionRegistrar.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnGuardarEstudiante = findViewById(R.id.btnGuardarEstudiante)
        btnGuardarEstudiante.setOnClickListener {
            if (editar) {
                actualizarEstudiante()  // Si estamos en modo edición, actualizamos el estudiante

            } else {
                registrarEstudiante()  // Si estamos registrando, creamos uno nuevo
            }
        }
        btnCancelarEstudiante = findViewById(R.id.btnCancelarEstudiante)
        btnCancelarEstudiante.setOnClickListener {
            val intent: Intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("codigo", nameadminRegistrar.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }

        // Spinner de Sexo
        regSexo = findViewById(R.id.regSexo)
        val sexoAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, listOf("M", "F", "Otro")
        )
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regSexo.adapter = sexoAdapter
    }

    private fun cargarProgramas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitCliente.webService.obtenerProgramas()
                if (response.isSuccessful) {
                    val programas = response.body()?.listaProgramas ?: emptyList()
                    val nombresProgramas = programas.map { it.nombre_programa }

                    val programasAdapter = ArrayAdapter(
                        this@RegistrarEstudiante,
                        android.R.layout.simple_spinner_item,
                        nombresProgramas
                    )
                    programasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    regPrograma.adapter = programasAdapter

                    // Seleccionar el programa del estudiante (modo edición)
                    val programaNombre = intent.getStringExtra("programa_nombre")
                    programaNombre?.let {
                        val programaIndex = programasAdapter.getPosition(it)
                        regPrograma.setSelection(if (programaIndex != -1) programaIndex else 0)
                    }
                } else {
                    mostrarMensaje("ATEC  |  ERROR", "No se pudieron cargar los programas.", false)
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
    }


    private fun registrarEstudiante() {
        val dni = regDNI.text.toString().trim().toIntOrNull()
        val nombres = regNombres.text.toString().trim()
        val apellidos = regApellidos.text.toString().trim()
        val codigo = regCodigo.text.toString().trim()
        val clave = regClave.text.toString().trim()
        val correo = regCorreo.text.toString().trim()
        val sexo = regSexo.selectedItem.toString()  // Obtiene el valor seleccionado
        val telefono = regTelefono.text.toString().trim()

        // Supón que el programa es representado por el índice seleccionado en el spinner.
        val programa = regPrograma.selectedItemPosition + 1  // El ID es el índice + 1

        // Validaciones adicionales
        if (dni == null || nombres.isBlank() || apellidos.isBlank() || codigo.isBlank() || clave.isBlank() || correo.isBlank() || programa == null || sexo.isBlank() || telefono.isBlank()) {
            mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.", false)
            return
        }

        val registroUsuario = RegistroUsuario(
            usu_dni = dni,
            usu_nombres = nombres,
            usu_apellidos = apellidos,
            usu_codigo = codigo,
            usu_clave = clave,
            usu_correo = correo,
            usu_sexo = sexo,
            usu_telefono = telefono,
            rol_id = 3,  // Asumiendo que 3 es para estudiantes
            programa_id = programa  // Enviar el ID del programa
        )

        lifecycleScope.launch {
            try {
                val response: Response<RegistroResponse> = RetrofitCliente.webService.registroUsuario(registroUsuario)
                if (response.isSuccessful) {
                    mostrarMensaje("ATEC  |  REGISTRO DE ESTUDIANTE", response.body()?.message ?: "Estudiante registrado correctamente", true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    when (response.code()) {
                        400 -> mostrarMensaje("ATEC  |  ERROR", "Datos incorrectos o incompletos: $errorBody", false)
                        409 -> mostrarMensaje("ATEC  |  ERROR", "Datos ya registrados", false)
                        else -> mostrarMensaje("ATEC  |  ERROR", "Registro fallido: ${response.code()} - $errorBody", false)
                    }
                }
            } catch (e: Exception) {
                mostrarMensaje("ATEC  |  ERROR", "Error en la conexión: ${e.message}", false)
            }
        }
    }


    private fun actualizarEstudiante() {
        val dni = regDNI.text.toString().trim().toIntOrNull()
        val nombres = regNombres.text.toString().trim()
        val apellidos = regApellidos.text.toString().trim()
        val codigo = regCodigo.text.toString().trim()
        val clave = regClave.text.toString().trim()
        val correo = regCorreo.text.toString().trim()
        val telefono = regTelefono.text.toString().trim()
        val sexo = regSexo.selectedItem.toString()

        val programa = regPrograma.selectedItemPosition + 1

        // Validar que todos los datos sean correctos
        if (dni == null || nombres.isBlank() || apellidos.isBlank() || codigo.isBlank() || correo.isBlank() || telefono.isBlank() || sexo.isBlank() || programa == null) {
            mostrarMensaje("ATEC  |  ERROR", "Faltan completar datos.", false)
            return
        }

        val usuario = Usuario(
            usu_dni = dni,
            usu_nombres = nombres,
            usu_apellidos = apellidos,
            usu_codigo = codigo,
            usu_clave = clave,
            usu_correo = correo,
            usu_sexo = sexo,
            usu_telefono = telefono,
            rol_id = 3,  // Asumiendo que 3 es para estudiantes
            programa_id = programa  // Enviar el ID del programa
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitCliente.webService.actualizarEstudiante(codigoEstudiante!!, usuario)
                if (response.isSuccessful) {
                    val mensaje = response.body()?.message ?: "Estudiante actualizado correctamente."
                    mostrarMensaje("ATEC  |  ACTUALIZACIÓN DE ESTUDIANTE", mensaje, true)
                } else {
                    val errorBody = response.errorBody()?.string()
                    mostrarMensaje("ATEC  |  ERROR", "Error al actualizar el estudiante: $errorBody", false)
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
                    intent.putExtra("codigo", nameadminRegistrar.text.toString()) // Pasar el código
                    startActivity(intent)
                    finish()
                }
            }
            .create()
            .show()
    }
}