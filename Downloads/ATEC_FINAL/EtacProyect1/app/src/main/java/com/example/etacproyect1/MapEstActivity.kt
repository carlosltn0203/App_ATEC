package com.example.etacproyect1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapEstActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnVolverMapEst: Button
    private lateinit var nameestudianteMap: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map_est)

        nameestudianteMap = findViewById(R.id.nameestudianteMap)
        //Recuperar código
        val codigo = intent.getStringExtra("codigo")
        nameestudianteMap.text = codigo ?: "Código no encontrado"

        // Configurar el botón
        btnVolverMapEst = findViewById(R.id.btnVolverMapEst)

        // Establecer el listener para el botón
        btnVolverMapEst.setOnClickListener {

            val intent = Intent(this, EstudianteActivity::class.java)
            intent.putExtra("codigo", nameestudianteMap.text.toString()) // Pasar el código
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar el fragmento del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapEst) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(mapEst: GoogleMap) {
        googleMap = mapEst

        // Configurar una ubicación de ejemplo, como Lima, Perú
        val location = LatLng(-12.149542424581318, -77.0047146288356)
        googleMap.addMarker(MarkerOptions().position(location).title("Santiago de Surco, Perú"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))

    }
}