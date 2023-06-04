package com.example.medicamomento

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.BaseColumns
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream


class AgregarMedicamento : AppCompatActivity() {

    private lateinit var et_Medicamento: EditText
    private lateinit var et_Dosis: EditText
    private lateinit var et_Fecha: EditText
    private lateinit var et_Hora: EditText
    private lateinit var sp_cant: Spinner

    var medicamento: String? = null
    var dosis: String? = null
    var fecha: String? = null
    var hora: String? = null
    var cant: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_medicamento)
        et_Fecha = findViewById(R.id.etxt_fecha)
        et_Fecha.setOnClickListener { showDatePickerDialog() }

        et_Hora = findViewById(R.id.etxt_hora)
        et_Hora.setOnClickListener { showTimePickerDialog() }

        /*cameraPermision = String[]{Manifest.permission.CAMERA, Manifest.Perm}*/
        val btnCamara = findViewById<ImageView>(R.id.btnCamara)
        btnCamara.setOnClickListener{
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment {onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "time")
    }
    private fun onTimeSelected(time:String){
        et_Hora.setText(time)
    }

    //Calendario
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datPicker")
    }
    fun onDateSelected(day: Int, month:Int, year: Int){
        et_Fecha.setText("$day/$month/$year")
    }


    /*botones*/
    fun CancelarCreacion(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
        }
        startActivity(intent)
    }
    fun validateMedicamento(): Boolean {
        var retorno = true

        et_Medicamento = findViewById(R.id.etxt_medicamento)
        et_Dosis = findViewById(R.id.etxt_dosis)
        et_Fecha = findViewById(R.id.etxt_fecha)
        et_Hora = findViewById(R.id.etxt_hora)
        sp_cant = findViewById(R.id.spinner)

        medicamento = et_Medicamento.text.toString()
        dosis = et_Dosis.text.toString()
        fecha = et_Fecha.text.toString()
        hora = et_Hora.text.toString()


        if (medicamento!!.isEmpty()) {
            et_Medicamento.setError("Campo Vacio")
            retorno = false
        }
        if (dosis!!.isEmpty()) {
            et_Dosis.setError("Campo Vacio")
            retorno = false
        }
        if (fecha!!.isEmpty()) {
            et_Fecha.setError("Campo Vacio")
            retorno = false
        }
        if (hora!!.isEmpty()) {
            et_Hora.setError("Campo Vacio")
            retorno = false
        }
        if (imageBitmapa == null) {
            Toast.makeText(this, "Agrega una imagen", Toast.LENGTH_SHORT).show()
            retorno = false
        }

        return retorno
    }

    fun GuardarMedicamento(view: View) {
        if (validateMedicamento()) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
            }

            val dbHelper = DBhelper(applicationContext)
            val db = dbHelper.writableDatabase
            cant = sp_cant.selectedItem.toString()

            val values = ContentValues().apply {
                put(Constants.medicinas.COLUMN_MEDICAMENTO, medicamento)
                put(Constants.medicinas.COLUMN_DOSIS, dosis + " " + cant)
                put(Constants.medicinas.COLUMN_FECHA, fecha)
                put(Constants.medicinas.COLUMN_HORARIO, hora)
            }

            val newRowId = db.insert(Constants.medicinas.TABLE_NAME, null, values)
            val idc = newRowId.toString()

            if (imageBitmapa != null) {
                val imageByteArray = convertBitmapToByteArray(imageBitmapa!!)
                val contentValues = ContentValues().apply {
                    put(Constants.medicinas.COLUMN_IMAGEN, imageByteArray)
                }
                db.update(
                    Constants.medicinas.TABLE_NAME,
                    contentValues,
                    "${BaseColumns._ID} = ?",
                    arrayOf(idc)
                )
            }

            db.close()

            Toast.makeText(applicationContext, idc, Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }
    }

    private var imageBitmapa: Bitmap? = null
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val imageBitmapa = intent?.extras?.get("data") as Bitmap?
            val imageView = findViewById<ImageView>(R.id.btnCamara)
            imageView.setImageBitmap(imageBitmapa)
            if (imageBitmapa != null) {
                this.imageBitmapa = imageBitmapa
            }
        }
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}