package com.example.etacproyect1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EstudianteActivity : AppCompatActivity() {
    private lateinit var btnCerrarSesionEst: ImageView
    private lateinit var nameestudiante: TextView
    private lateinit var nameestudiante2: TextView
    private lateinit var btnVerHorarios: Button
    private lateinit var btnVerProgramaEstudios: Button
    private lateinit var btnVerCertificados: Button
    private lateinit var btnSitioWebEst: ImageView
    private lateinit var btnAulaVirtualEst: ImageView
    private lateinit var btnSedeEst: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estudiante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()

        val codigo = intent.getStringExtra("codigo")
        nameestudiante.text = codigo ?: "Código no encontrado"
    }

    private fun asignarReferencias(){
        btnCerrarSesionEst = findViewById(R.id.btnCerrarSesionEst)
        btnCerrarSesionEst .setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        nameestudiante = findViewById(R.id.nameestudiante)
        nameestudiante2 = findViewById(R.id.nameestudiante2)
        btnVerHorarios = findViewById(R.id.btnVerHorarios)
        btnVerHorarios.setOnClickListener {
            val intent = Intent(this, HorariosActivity::class.java)
            intent.putExtra("codigo", nameestudiante.text.toString()) // Pasar el código
            startActivity(intent)
        }
        btnVerProgramaEstudios = findViewById(R.id.btnVerProgramaEstudios)
        btnVerProgramaEstudios.setOnClickListener {
            val intent = Intent(this, CursosActivity::class.java)
            intent.putExtra("codigo", nameestudiante.text.toString()) // Pasar el código
            startActivity(intent)
        }
        btnVerCertificados = findViewById(R.id.btnVerCertificados)
        btnVerCertificados.setOnClickListener {
            val intent = Intent(this, CertificadosActivity::class.java)
            intent.putExtra("codigo", nameestudiante.text.toString()) // Pasar el código
            startActivity(intent)
        }
        btnSitioWebEst = findViewById(R.id.btnSitioWebEst)
        btnSitioWebEst.setOnClickListener {
            val url = "https://academiatecnologicainternacional.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnAulaVirtualEst = findViewById(R.id.btnAulaVirtualEst)
        btnAulaVirtualEst.setOnClickListener {
            val url = "https://atecperu.q10.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        btnSedeEst = findViewById(R.id.btnSedeEst)
        btnSedeEst.setOnClickListener {
            val intent: Intent = Intent(this, MapEstActivity::class.java)
            intent.putExtra("codigo", nameestudiante.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }
    }
}