package com.example.etacproyect1

import android.content.Intent
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

class DocenteActivity : AppCompatActivity() {
    private lateinit var btnCerrarSesionDoc:ImageView
    private lateinit var busquedaEstudianteDoc: SearchView
    private lateinit var namedocente: TextView
    private lateinit var namedocente2: TextView
    private lateinit var btnVerClasesDoc: Button
    private lateinit var btnVerListaDoc: Button
    private lateinit var btnVerAprobarDesaprobarDoc: Button
    private lateinit var btnSitioWebDoc: ImageView
    private lateinit var btnAulaVirtualDoc: ImageView
    private lateinit var btnSedeDoc: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_docente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()

        // Muestra el nombre del docente (asume que tienes el nombre y apellido)
        val codigo = intent.getStringExtra("codigo")
        namedocente.text = codigo ?: "Código no encontrado"

        configurarBusqueda()
    }

    private fun asignarReferencias() {
        btnCerrarSesionDoc = findViewById(R.id.btnCerrarSesionDoc)
        btnCerrarSesionDoc.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        namedocente = findViewById(R.id.namedocente)
        namedocente2 = findViewById(R.id.namedocente2)
        btnVerClasesDoc = findViewById(R.id.btnVerClasesDoc)
        btnVerListaDoc = findViewById(R.id.btnVerListaDoc)
        btnVerListaDoc.setOnClickListener {
            val intent = Intent(this, VerListaEstudiantes::class.java)
            intent.putExtra("codigo", namedocente.text.toString()) // Pasa el código
            startActivity(intent)
        }
        btnVerAprobarDesaprobarDoc = findViewById(R.id.btnVerAprobarDesaprobarDoc)
        btnVerAprobarDesaprobarDoc.setOnClickListener {
            val intent = Intent(this, VerAprobarDesaprobar::class.java)
            intent.putExtra("codigo", namedocente.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }

        // Configura el botón para ver clases
        btnVerClasesDoc.setOnClickListener {
            val intent = Intent(this, VerClasesDocActivity::class.java)
            intent.putExtra("codigo", namedocente.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnSitioWebDoc = findViewById(R.id.btnSitioWebDoc)
        btnSitioWebDoc.setOnClickListener {
            val url = "https://academiatecnologicainternacional.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnAulaVirtualDoc = findViewById(R.id.btnAulaVirtualDoc)
        btnAulaVirtualDoc.setOnClickListener {
            val url = "https://atecperu.q10.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnSedeDoc = findViewById(R.id.btnSedeDoc)
        btnSedeDoc.setOnClickListener {
            val intent: Intent = Intent(this, MapDocActivity::class.java)
            intent.putExtra("codigo", namedocente.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
    }

    private fun configurarBusqueda() {
        busquedaEstudianteDoc = findViewById(R.id.busquedaEstudianteDoc)

        busquedaEstudianteDoc.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                        Toast.makeText(this@DocenteActivity, "Estudiante no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("DocenteActivity", "Error en la búsqueda: ${response.errorBody()?.string()}")
                    Toast.makeText(this@DocenteActivity, "Error en la búsqueda", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("DocenteActivity", "Excepción: ${e.message}")
                Toast.makeText(this@DocenteActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarEstudiante(estudiante: Estudiante) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ATEC  |  ESTUDIANTE: ${estudiante.usu_codigo}")

        val mensaje = """
            DNI: ${estudiante.usu_dni}
            Nombres: ${estudiante.usu_nombres}
            Apellidos: ${estudiante.usu_apellidos}
            Correo ATEC: ${estudiante.usu_correo}
            Programa de Estudio: ${estudiante.nombrePrograma}
            Sexo: ${estudiante.usu_sexo}
            Teléfono: ${estudiante.usu_telefono}
        """.trimIndent()

        builder.setMessage(mensaje)

        builder.setPositiveButton("CERRAR") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}