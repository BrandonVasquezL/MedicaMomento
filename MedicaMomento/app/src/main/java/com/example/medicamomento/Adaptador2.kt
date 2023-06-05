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

    private var selectedPosition = RecyclerView.NO_POSITION

    fun updateSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }
    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinaitem2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicamento = medicamentos[position]
        holder.bind(medicamento)
        holder.itemView.isActivated = position == selectedPosition
        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
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
    fun getMedicamentoAtPosition(position: Int): DBhelper.Medicamento? {
        if (position != RecyclerView.NO_POSITION) {
            return medicamentos[position]
        }
        return null
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
            val bitmap = BitmapFactory.decodeByteArray(medicamento.imagen, 0, medicamento.imagen.size)
            imgmed.setImageBitmap(bitmap)
            txtNombre.text = medicamento.nombre
            txtDosis.text = "Tomar: " + medicamento.dosis
            txtHorario.text = "Tomar cada: " + medicamento.horario
        }


    }

}