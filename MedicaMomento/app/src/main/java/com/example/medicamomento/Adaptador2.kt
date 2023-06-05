package com.example.medicamomento

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class Adaptador2(private val medicamentos: List<DBhelper.Medicamento>) : RecyclerView.Adapter<Adaptador2.ViewHolder>() {

    private var hasData: Boolean = medicamentos.isNotEmpty()
    private var showItems: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinaitem2, parent, false)
        view.visibility = if (showItems) View.VISIBLE else View.GONE
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicamento = medicamentos[position]
        holder.bind(medicamento)
    }
    fun showItems(show: Boolean) {
        showItems = show
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (showItems) medicamentos.size else 0
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.txt_name)
        private val txtDosis: TextView = itemView.findViewById(R.id.txt_dosis)
        private val txtHorario: TextView = itemView.findViewById(R.id.txt_time)
        private val imgmed: ImageView = itemView.findViewById(R.id.imgMedicamento)

        val btnBorrar: ImageView = itemView.findViewById(R.id.btnDelete)

        init {
            btnBorrar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val medicamento = medicamentos[position]
                    // Realiza las acciones correspondientes al hacer clic en el botón de eliminar
                }
            }
        }

        fun bind(medicamento: DBhelper.Medicamento) {
            // Realiza la vinculación de datos con los elementos de la vista
        }

    }
}
