package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.entidades.Estudiante
import com.example.etacproyect1.entidades.Horario
import com.example.etacproyect1.entidades.ListaEstudianteDoc
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerListaEstudiantes : AppCompatActivity() {

    private lateinit var namedocenteListaEst: TextView
    private lateinit var btnVolverLista: Button
    private lateinit var btnCerrarSesionListaEst: ImageView
    private lateinit var rvEstudiantes: RecyclerView
    private var adaptador: AdaptadorEstudiantes = AdaptadorEstudiantes()
    private var listaEstudiantesDoc: ArrayList<ListaEstudianteDoc> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_lista_estudiantes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()
        // Recuperar el código del estudiante del Intent
        val codigo = intent.getStringExtra("codigo")
        namedocenteListaEst.text = codigo ?: "Código no encontrado" // Mostrar el código
        cargarDatos()

    }

    private fun asignarReferencias(){
        namedocenteListaEst = findViewById(R.id.namedocenteListaEst) // Inicializar el TextView aquí
        rvEstudiantes = findViewById(R.id.rvEstudiantes)
        rvEstudiantes.layoutManager = LinearLayoutManager(this)
        btnVolverLista = findViewById(R.id.btnVolverLista)
        btnVolverLista.setOnClickListener {
            val intent = Intent(this, DocenteActivity::class.java)
            intent.putExtra("codigo", namedocenteListaEst.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionListaEst = findViewById(R.id.btnCerrarSesionListaEst)
        btnCerrarSesionListaEst.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerListaEstudiantes()
            runOnUiThread {
                if (rpta.isSuccessful) {
                    listaEstudiantesDoc = rpta.body()?.listaEstudiantesDoc ?: ArrayList()
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos() {
        adaptador.agregarDatos(listaEstudiantesDoc)
        rvEstudiantes.adapter = adaptador
    }
}