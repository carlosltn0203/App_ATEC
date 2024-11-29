package com.example.etacproyect1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.AdaptadorPersonalizado.MiViewHolder
import com.example.etacproyect1.entidades.Curso
import com.example.etacproyect1.entidades.Horario

class AdaptadorCursos: RecyclerView.Adapter<AdaptadorCursos.MiViewHolder>() {

    private var listaCursos: ArrayList<Curso> = ArrayList()

    fun agregarDatos(items: ArrayList<Curso>) {
        this.listaCursos = items
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var filaNombreCurso = view.findViewById<TextView>(R.id.filaNombreCurso)
        var filaPrograma = view.findViewById<TextView>(R.id.filaPrograma)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.curso, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val curso = listaCursos[position]
        holder.filaNombreCurso.text = curso.nombre_curso // Muestra el nombre del curso
        holder.filaPrograma.text = curso.nombrePrograma
    }

    override fun getItemCount(): Int {
        return listaCursos.size
    }
}