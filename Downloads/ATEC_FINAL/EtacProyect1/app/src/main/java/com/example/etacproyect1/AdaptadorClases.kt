package com.example.etacproyect1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.entidades.Clase

class AdaptadorClases: RecyclerView.Adapter<AdaptadorClases.MiViewHolder>() {

    private var listaClases: ArrayList<Clase> = ArrayList()

    fun agregarDatos(items: ArrayList<Clase>) {
        this.listaClases = items
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var filaProgramaClase = view.findViewById<TextView>(R.id.filaProgramaClase)
        var filaCursoClase = view.findViewById<TextView>(R.id.filaCursoClase)
        var filaFechaInicioClase = view.findViewById<TextView>(R.id.filaFechaInicioClase)
        var filaFechaFinClase = view.findViewById<TextView>(R.id.filaFechaFinClase)
        var filaDiaClase = view.findViewById<TextView>(R.id.filaDiaClase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.clase, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val clase = listaClases[position]
        holder.filaProgramaClase.text = clase.nombre_programa
        holder.filaCursoClase.text = clase.nombre_curso
        holder.filaFechaInicioClase.text = clase.hora_inicio
        holder.filaFechaFinClase.text = clase.hora_fin
        holder.filaDiaClase.text = clase.dia
    }

    override fun getItemCount(): Int {
        return listaClases.size
    }
}