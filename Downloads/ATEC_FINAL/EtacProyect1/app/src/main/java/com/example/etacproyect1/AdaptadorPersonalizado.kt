package com.example.etacproyect1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.entidades.Horario

class AdaptadorPersonalizado:RecyclerView.Adapter<AdaptadorPersonalizado.MiViewHolder>() {

    private var listaHorarios: ArrayList<Horario> = ArrayList()


    fun agregarDatos(items: ArrayList<Horario>) {
        this.listaHorarios = items
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var filaCurso = view.findViewById<TextView>(R.id.filaCurso)
        var filaDia = view.findViewById<TextView>(R.id.filaDia)
        var filaHoraInicio = view.findViewById<TextView>(R.id.filaHoraInicio)
        var filaHoraFin = view.findViewById<TextView>(R.id.filaHoraFin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horario, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val horario = listaHorarios[position]
        holder.filaCurso.text = horario.nombreCurso // Muestra el nombre del curso
        holder.filaDia.text = horario.dia
        holder.filaHoraInicio.text = horario.hora_inicio
        holder.filaHoraFin.text = horario.hora_fin
    }


    override fun getItemCount(): Int {
        return listaHorarios.size
    }
}