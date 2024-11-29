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

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnVolverMap: Button
    private lateinit var nameadminMap: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        nameadminMap = findViewById(R.id.nameadminMap)
        //Recuperar código
        val codigo = intent.getStringExtra("codigo")
        nameadminMap.text = codigo ?: "Código no encontrado"

        // Configurar el botón
        btnVolverMap = findViewById(R.id.btnVolverMap)

        // Establecer el listener para el botón
        btnVolverMap.setOnClickListener {

            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("codigo", nameadminMap.text.toString()) // Pasar el código
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
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configurar una ubicación de ejemplo, como Lima, Perú
        val location = LatLng(-12.149542424581318, -77.0047146288356)
        googleMap.addMarker(MarkerOptions().position(location).title("Santiago de Surco, Perú"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))

    }
}