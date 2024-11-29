package com.example.etacproyect1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etacproyect1.AdaptadorCursos.MiViewHolder
import com.example.etacproyect1.entidades.Certificado
import com.example.etacproyect1.entidades.Curso

class AdaptadorCertificados: RecyclerView.Adapter<AdaptadorCertificados.MiViewHolder>() {

    private var listaCertificados: ArrayList<Certificado> = ArrayList()

    fun agregarDatos(items: ArrayList<Certificado>) {
        this.listaCertificados = items
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var filaEstudianteCer = view.findViewById<TextView>(R.id.filaEstudianteCer)
        var filaCursoCer = view.findViewById<TextView>(R.id.filaCursoCer)
        var filaEmisionCer = view.findViewById<TextView>(R.id.filaEmisionCer)
        var filaUrlCer = view.findViewById<TextView>(R.id.filaUrlCer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.certificado, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val certificado = listaCertificados[position]
        holder.filaEstudianteCer.text = certificado.estudianteCodigo // Muestra el nombre del curso
        holder.filaCursoCer.text = certificado.estudianteCurso
        holder.filaEmisionCer.text = certificado.fecha_emision
        holder.filaUrlCer.text = certificado.certificado_url
    }

    override fun getItemCount(): Int {
        return listaCertificados.size
    }
}