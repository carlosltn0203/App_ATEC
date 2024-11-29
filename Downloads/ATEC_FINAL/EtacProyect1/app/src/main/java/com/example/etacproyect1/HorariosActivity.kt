package com.example.etacproyect1

import android.content.Intent
import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.etacproyect1.entidades.Horario
import com.example.etacproyect1.servicio.HorarioResponse
import com.example.etacproyect1.servicio.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class HorariosActivity : AppCompatActivity() {

    private lateinit var nameestudiantehor: TextView
    private lateinit var btnVolverEstHor: Button
    private lateinit var btnCerrarSesionHor: ImageView
    private lateinit var rvHorarios: RecyclerView
    private var adaptador: AdaptadorPersonalizado = AdaptadorPersonalizado()
    private var listaHorarios: ArrayList<Horario> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_horarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        asignarReferencias()
        // Recuperar el código del estudiante del Intent
        val codigo = intent.getStringExtra("codigo")
        nameestudiantehor.text = codigo ?: "Código no encontrado" // Mostrar el código
        cargarDatos()

    }

    private fun asignarReferencias(){
        nameestudiantehor = findViewById(R.id.nameestudiantehor) // Inicializar el TextView aquí
        rvHorarios = findViewById(R.id.rvHorarios)
        rvHorarios.layoutManager = LinearLayoutManager(this)
        btnVolverEstHor = findViewById(R.id.btnVolverEstHor)
        btnVolverEstHor.setOnClickListener {
            val intent = Intent(this, EstudianteActivity::class.java)
            intent.putExtra("codigo", nameestudiantehor.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
        btnCerrarSesionHor = findViewById(R.id.btnCerrarSesionHor)
        btnCerrarSesionHor.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerHorarios()
            runOnUiThread {
                if (rpta.isSuccessful) {
                    listaHorarios = rpta.body()?.listaHorarios ?: ArrayList()
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos() {
        adaptador.agregarDatos(listaHorarios)
        rvHorarios.adapter = adaptador
    }
}