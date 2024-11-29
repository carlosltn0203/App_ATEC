package com.example.etacproyect1

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.etacproyect1.entidades.Estudiante
import com.example.etacproyect1.servicio.EstudianteResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {
    private lateinit var btnRegistrarEstudiante: Button
    private lateinit var btnCerrarSesion: ImageView
    private lateinit var busquedaEstudiante: SearchView
    private lateinit var nameadmin: TextView
    private lateinit var nameadmin2: TextView
    private lateinit var btnSitioWeb: ImageView
    private lateinit var btnAulaVirtual: ImageView
    private lateinit var btnSede: ImageView
    private lateinit var btnRegistrarProgramaAdmin:Button
    private lateinit var btnRegistrarCursoAdmin:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()

        val codigo = intent.getStringExtra("codigo")
        nameadmin.text = codigo ?: "Código no encontrado"

        configurarBusqueda()
    }

    private fun asignarReferencias(){
        btnRegistrarEstudiante = findViewById(R.id.btnRegistrarEstudiante)
        btnRegistrarEstudiante.setOnClickListener {
            val intent: Intent = Intent(this, RegistrarEstudiante::class.java)
            intent.putExtra("codigo", nameadmin.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        nameadmin = findViewById(R.id.nameadmin)
        nameadmin2 = findViewById(R.id.nameadmin2)
        btnSitioWeb = findViewById(R.id.btnSitioWeb)
        btnSitioWeb.setOnClickListener {
            val url = "https://academiatecnologicainternacional.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnAulaVirtual = findViewById(R.id.btnAulaVirtual)
        btnAulaVirtual.setOnClickListener {
            val url = "https://atecperu.q10.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnSede = findViewById(R.id.btnSede)
        btnSede.setOnClickListener {
            val intent: Intent = Intent(this, MapActivity::class.java)
            intent.putExtra("codigo", nameadmin.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnRegistrarProgramaAdmin = findViewById(R.id.btnRegistrarProgramaAdmin)
        btnRegistrarProgramaAdmin.setOnClickListener {
            val intent: Intent = Intent(this, RegistrarProgramaEstudio::class.java)
            intent.putExtra("codigo", nameadmin.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnRegistrarCursoAdmin = findViewById(R.id.btnRegistrarCursoAdmin)
        btnRegistrarCursoAdmin.setOnClickListener {
            val intent: Intent = Intent(this, RegistrarCursosActivity::class.java)
            intent.putExtra("codigo", nameadmin.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
    }

    private fun configurarBusqueda() {
        busquedaEstudiante = findViewById(R.id.busquedaEstudiante)

        busquedaEstudiante.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { buscarEstudiante(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun buscarEstudiante(codigo: String) {
        lifecycleScope.launch {
            try {
                // Llamar al metodo buscarEstudiante en Retrofit
                val response = RetrofitCliente.webService.buscarEstudiante(codigo)
                if (response.isSuccessful) {
                    val estudiante = response.body()
                    if (estudiante != null) {
                        // Mostrar los datos del estudiante o actualizarlos en la UI
                        mostrarEstudiante(estudiante)
                    } else {
                        mostrarDialogo("Información", "Estudiante no encontrado")
                    }
                } else {
                    Log.e("AdminActivity", "Error en la búsqueda: ${response.errorBody()?.string()}")
                    mostrarDialogo("Error", "Error en la búsqueda.")
                }
            } catch (e: Exception) {
                Log.e("AdminActivity", "Excepción: ${e.message}")
                mostrarDialogo("Error", "Error de conexión.")
            }
        }
    }

    private fun mostrarEstudiante(estudiante: Estudiante) {
        // Crear el cuadro de diálogo
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ATEC  |  ESTUDIANTE: ${estudiante.usu_codigo}")

        // Concatenar todos los datos en un solo mensaje
        val mensaje = """
            DNI: ${estudiante.usu_dni}
            Nombres: ${estudiante.usu_nombres}
            Apellidos: ${estudiante.usu_apellidos}
            Correo ATEC: ${estudiante.usu_correo}
            Programa de Estudio: ${estudiante.nombrePrograma}
            Sexo: ${estudiante.usu_sexo}
            Teléfono: ${estudiante.usu_telefono}
        """.trimIndent()

        builder.setMessage(mensaje)  // Establecer el mensaje concatenado


        // Botón de editar
        builder.setNeutralButton("EDITAR") { dialog, _ ->
            // Redirigir a RegistrarEstudiante con los datos para editar
            val intent = Intent(this, RegistrarEstudiante::class.java).apply {
                putExtra("usu_dni", estudiante.usu_dni.toString())  // Enviar el DNI como String
                putExtra("usu_nombres", estudiante.usu_nombres)
                putExtra("usu_apellidos", estudiante.usu_apellidos)
                putExtra("usu_codigo", estudiante.usu_codigo)
                putExtra("usu_clave", estudiante.usu_clave)
                putExtra("usu_correo", estudiante.usu_correo)
                putExtra("usu_telefono", estudiante.usu_telefono)
                putExtra("usu_sexo", estudiante.usu_sexo)
                putExtra("programa_nombre", estudiante.nombrePrograma)  // Aquí es donde pasas el nombre del programa
                putExtra("codigo", nameadmin.text.toString()) // Pasar el código del administrador
            }
            startActivity(intent)
        }

        // Botón de eliminar
        builder.setNegativeButton("REMOVER") { dialog, _ ->
            // Confirmar eliminación
            confirmarEliminarEstudiante(estudiante.usu_codigo)
            dialog.dismiss()
        }

        // Botón de cerrar
        builder.setPositiveButton("CERRAR") { dialog, _ ->
            dialog.dismiss()  // Cierra el cuadro de diálogo al presionar "Cerrar"
        }

        // Mostrar el cuadro de diálogo
        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarEstudiante(codigoEstudiante: String) {
        lifecycleScope.launch {
            try {
                // Llamamos al metodo eliminarEstudiante en el servicio WebService
                val response = RetrofitCliente.webService.eliminarEstudiante(codigoEstudiante)

                if (response.isSuccessful) {
                    // Si el servidor responde con éxito (código 200)
                    val mensajeExito = response.body() ?: "Se eliminó el estudiante correctamente."
                    mostrarDialogo("Éxito", mensajeExito)
                } else {
                    // Si el servidor responde con un error (por ejemplo, 404 o 500)
                    val mensajeError = response.errorBody()?.string() ?: "Error al eliminar el estudiante."
                    mostrarDialogo("Error", mensajeError)
                }
            } catch (e: Exception) {
                // Maneja cualquier excepción (como problemas de red)
                mostrarDialogo("Error", "Error de conexión")
            }
        }
    }

    private fun confirmarEliminarEstudiante(codigoEstudiante: String) {
        AlertDialog.Builder(this)
            .setTitle("ATEC | REMOVER ESTUDIANTE")
            .setMessage("¿Estás seguro de que deseas remover a este estudiante?")
            .setPositiveButton("REMOVER") { dialog, _ ->
                // Llamar a eliminarEstudiante si el usuario confirma
                eliminarEstudiante(codigoEstudiante)
                dialog.dismiss()
            }
            .setNegativeButton("CANCELAR") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this@AdminActivity)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("ACEPTAR") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo al pulsar el botón
            }
            .show()
    }

}