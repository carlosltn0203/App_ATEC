package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.entidades.Certificado
import com.example.etacproyect1.entidades.Curso
import com.example.etacproyect1.servicio.CertificadoResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CertificadosActivity : AppCompatActivity() {
    private lateinit var nameestudiantecer: TextView
    private lateinit var btnVolverEstCer: Button
    private lateinit var btnCerrarSesionCer: ImageView
    private lateinit var rvCertificados: RecyclerView
    private var adaptador: AdaptadorCertificados = AdaptadorCertificados()
    private var listaCertificados: ArrayList<Certificado> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_certificados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()

        // Recuperar el código del estudiante del Intent
        val codigo = intent.getStringExtra("codigo")
        nameestudiantecer.text = codigo ?: "Código no encontrado"  // Mostrar el código

        cargarDatos()
    }

    private fun asignarReferencias() {
        nameestudiantecer = findViewById(R.id.nameestudiantecer) // Inicializar el TextView aquí
        rvCertificados = findViewById(R.id.rvCertificados)
        rvCertificados.layoutManager = LinearLayoutManager(this)
        btnVolverEstCer = findViewById(R.id.btnVolverEstCer)
        btnVolverEstCer.setOnClickListener {
            val intent = Intent(this, EstudianteActivity::class.java)
            intent.putExtra("codigo", nameestudiantecer.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionCer = findViewById(R.id.btnCerrarSesionCer)
        btnCerrarSesionCer.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatos() {
        val codigo = intent.getStringExtra("codigo") // Obtener el código del estudiante desde el Intent
        if (codigo.isNullOrEmpty()) {
            // Si no se proporciona el código, mostramos un mensaje de error
            Toast.makeText(this, "Código de estudiante no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        // Hacemos la llamada a la API para obtener los certificados
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Hacemos la solicitud a la API, pasando el código del estudiante
                val rpta: Response<CertificadoResponse> = RetrofitCliente.webService.obtenerCertificados()

                // Verificamos si la respuesta fue exitosa
                withContext(Dispatchers.Main) {
                    if (rpta.isSuccessful) {
                        // Si la respuesta es exitosa, obtenemos los certificados
                        listaCertificados = rpta.body()?.listaCertificados ?: ArrayList()
                        // Actualizamos la vista con los certificados
                        mostrarDatos()
                    } else {
                        // Si no se encontraron certificados, mostramos un mensaje
                        Toast.makeText(this@CertificadosActivity, "No se encontraron certificados", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Si hay algún error en la solicitud o en la red, mostramos un mensaje de error
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CertificadosActivity, "Error al cargar los certificados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarDatos() {
        adaptador.agregarDatos(listaCertificados)
        rvCertificados.adapter = adaptador
    }
}