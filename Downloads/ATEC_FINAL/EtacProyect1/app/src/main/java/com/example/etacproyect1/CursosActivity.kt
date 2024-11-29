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
import com.example.etacproyect1.entidades.Curso
import com.example.etacproyect1.entidades.Horario
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CursosActivity : AppCompatActivity() {
    private lateinit var nameestudiantecur: TextView
    private lateinit var btnVolverEstCur: Button
    private lateinit var btnCerrarSesionCur: ImageView
    private lateinit var cursosEstudiante: TextView
    private lateinit var rvCursos: RecyclerView
    private var adaptador: AdaptadorCursos = AdaptadorCursos()
    private var listaCursos: ArrayList<Curso> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cursos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
        // Recuperar el código del estudiante del Intent
        val codigo = intent.getStringExtra("codigo")
        nameestudiantecur.text = codigo ?: "Código no encontrado" // Mostrar el código
        cargarDatos()
    }

    private fun asignarReferencias() {
        nameestudiantecur = findViewById(R.id.nameestudiantecur) // Inicializar el TextView aquí
        rvCursos = findViewById(R.id.rvCursos)
        rvCursos.layoutManager = LinearLayoutManager(this)
        btnVolverEstCur = findViewById(R.id.btnVolverEstCur)
        btnVolverEstCur.setOnClickListener {
            val intent = Intent(this, EstudianteActivity::class.java)
            intent.putExtra("codigo", nameestudiantecur.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionCur = findViewById(R.id.btnCerrarSesionCur)
        btnCerrarSesionCur.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerCursos()
            runOnUiThread {
                if (rpta.isSuccessful) {
                    listaCursos = rpta.body()?.listaCursos ?: ArrayList()
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos() {
        adaptador.agregarDatos(listaCursos)
        rvCursos.adapter = adaptador
    }
}