package com.example.medicamomento

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.BaseColumns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class EditarMedicina : AppCompatActivity() {
    private lateinit var edNombre: TextView
    private lateinit var edDosis: TextView
    private lateinit var edFecha: TextView
    private lateinit var edHora: TextView
    private lateinit var btncancelar: Button
    private lateinit var btnGuardar: Button

    var medicamento: String? = null
    var dosis: String? = null
    var fecha: String? = null
    var hora: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_medicina)

        edNombre = findViewById(R.id.edNombre)
        edDosis = findViewById(R.id.edDosis)
        edFecha = findViewById(R.id.edFecha)
        edHora = findViewById(R.id.edHorario)
        btncancelar = findViewById(R.id.btncancelar)
        btnGuardar = findViewById(R.id.btneditado)

        val extras = intent.extras
        medicamento = extras?.getString("medicamento")
        dosis = extras?.getString("dosis")
        fecha = extras?.getString("fecha")
        hora = extras?.getString("hora")

        edHora.setOnClickListener { showTimePickerDialog() }
        edFecha.setOnClickListener { showDatePickerDialog() }
        btncancelar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
            }
            startActivity(intent)
        }
        btnGuardar.setOnClickListener {
            val nuevoMedicamento = edNombre.text.toString()
            val nuevaDosis = edDosis.text.toString()
            val nuevaFecha = edFecha.text.toString()
            val nuevaHora = edHora.text.toString()

            val dbHelper = DBhelper(applicationContext)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(Constants.medicinas.COLUMN_MEDICAMENTO, nuevoMedicamento)
                put(Constants.medicinas.COLUMN_DOSIS, nuevaDosis)
                put(Constants.medicinas.COLUMN_FECHA, nuevaFecha)
                put(Constants.medicinas.COLUMN_HORARIO, nuevaHora)
            }
            val idMedicamento = extras?.getInt("id") // Obtén el ID del medicamento a editar
            val selection = "${BaseColumns._ID} = ?" // Define la cláusula WHERE para seleccionar el medicamento por su ID
            val selectionArgs = arrayOf(idMedicamento.toString()) // Especifica el valor del ID como argumento de selección

            val numRowsUpdated = db.update(
                Constants.medicinas.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )

            if (numRowsUpdated > 0) {
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }

            db.close()
        }

        edNombre.setText(medicamento)
        edDosis.setText(dosis)
        edFecha.setText(fecha)
        edHora.setText(hora)
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment {onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun onTimeSelected(time:String){
        edHora.setText(time)
    }

    //Calendario
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datPicker")
    }

    fun onDateSelected(day: Int, month:Int, year: Int){
        edFecha.setText("$day/$month/$year")
    }
}

