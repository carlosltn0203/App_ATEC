package com.example.etacproyect1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.entidades.Curso
import com.example.etacproyect1.entidades.Estudiante
import com.example.etacproyect1.entidades.ListaEstudianteDoc

class AdaptadorEstudiantes: RecyclerView.Adapter<AdaptadorEstudiantes.MiViewHolder>() {

    private var listaEstudiantesDoc: ArrayList<ListaEstudianteDoc> = ArrayList()

    fun agregarDatos(items: ArrayList<ListaEstudianteDoc>) {
        this.listaEstudiantesDoc = items
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var filaCodigoLista = view.findViewById<TextView>(R.id.filaCodigoLista)
        var filaNombreLista = view.findViewById<TextView>(R.id.filaNombreLista)
        var filaApellidoLista = view.findViewById<TextView>(R.id.filaApellidoLista)
        var filaProgramaLista = view.findViewById<TextView>(R.id.filaProgramaLista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.estudiante, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val estudiante = listaEstudiantesDoc[position]
        holder.filaCodigoLista.text = estudiante.usu_codigo // Muestra el nombre del curso
        holder.filaNombreLista.text = estudiante.usu_nombres
        holder.filaApellidoLista.text = estudiante.usu_apellidos
        holder.filaProgramaLista.text = estudiante.nombrePrograma
    }

    override fun getItemCount(): Int {
        return listaEstudiantesDoc.size
    }
}