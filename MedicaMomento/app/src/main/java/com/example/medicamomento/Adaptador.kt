package com.example.medicamomento

import android.content.Context
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class Adaptador(private val medicamentos: List<DBhelper.Medicamento>) : RecyclerView.Adapter<Adaptador.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinaitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicamento = medicamentos[position]
        holder.bind(medicamento)
    }

    override fun getItemCount(): Int {
        return medicamentos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.txt_name)
        private val txtDosis: TextView = itemView.findViewById(R.id.txt_dosis)
        private val txtHorario: TextView = itemView.findViewById(R.id.txt_time)



        fun bind(medicamento: DBhelper.Medicamento) {
            txtNombre.text = medicamento.nombre
            txtDosis.text = "Tomar: " + medicamento.dosis
            txtHorario.text = "Tomar cada: " + medicamento.horario
        }
    }

}