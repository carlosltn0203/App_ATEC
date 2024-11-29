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
import com.example.etacproyect1.entidades.Clase
import com.example.etacproyect1.entidades.ListaEstudianteDoc
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerClasesDocActivity : AppCompatActivity() {
    private lateinit var namedocenteClases: TextView
    private lateinit var btnVolverClases: Button
    private lateinit var btnCerrarSesionClases: ImageView
    private lateinit var rvClases: RecyclerView
    private var adaptador: AdaptadorClases = AdaptadorClases()
    private var listaClases: ArrayList<Clase> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_clases_doc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()
        // Recuperar el código del estudiante del Intent
        val codigo = intent.getStringExtra("codigo")
        namedocenteClases.text = codigo ?: "Código no encontrado" // Mostrar el código
        cargarDatos()

    }

    private fun asignarReferencias(){
        namedocenteClases = findViewById(R.id.namedocenteClases) // Inicializar el TextView aquí
        rvClases = findViewById(R.id.rvClases)
        rvClases.layoutManager = LinearLayoutManager(this)
        btnVolverClases = findViewById(R.id.btnVolverClases)
        btnVolverClases.setOnClickListener {
            val intent = Intent(this, DocenteActivity::class.java)
            intent.putExtra("codigo", namedocenteClases.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionClases = findViewById(R.id.btnCerrarSesionClases)
        btnCerrarSesionClases.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerClases()
            runOnUiThread {
                if (rpta.isSuccessful) {
                    listaClases = rpta.body()?.listaClases ?: ArrayList()
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos() {
        adaptador.agregarDatos(listaClases)
        rvClases.adapter = adaptador
    }
}