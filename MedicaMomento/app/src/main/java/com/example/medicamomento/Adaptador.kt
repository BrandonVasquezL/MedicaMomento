package com.example.medicamomento

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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
    fun deleteData(context: Context, id: Int): Boolean {
        val database = DBhelper.getInstance(context)
        val db = database.writableDatabase

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val deletedRows = db.delete(Constants.medicinas.TABLE_NAME, selection, selectionArgs)

        db.close()

        return deletedRows > 0
    }

    override fun getItemCount(): Int {
        return medicamentos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.txt_name)
        private val txtDosis: TextView = itemView.findViewById(R.id.txt_dosis)
        private val txtHorario: TextView = itemView.findViewById(R.id.txt_time)
        val btnBorrar: ImageView = itemView.findViewById(R.id.btnDelete)

        init {
            btnBorrar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val medicamento = medicamentos[position]
                    val idToDelete = medicamento.id // Obtener el ID del medicamento a borrar
                    val nomMed = medicamento.nombre
                    val deleted = deleteData(itemView.context, idToDelete)
                    if (deleted) {
                       Toast.makeText(itemView.context, "Borraste  el  $nomMed", Toast.LENGTH_SHORT).show()
                        val intent = Intent(itemView.context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        val context = itemView.context
                        if (context is Activity) {
                            context.startActivity(intent)
                            context.overridePendingTransition(0, 0)
                        }

                    } else {
                        Toast.makeText(itemView.context, "Seleccionaste $idToDelete", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        fun bind(medicamento: DBhelper.Medicamento) {
            txtNombre.text = medicamento.nombre
            txtDosis.text = "Tomar: " + medicamento.dosis
            txtHorario.text = "Tomar cada: " + medicamento.horario

        }
    }

}