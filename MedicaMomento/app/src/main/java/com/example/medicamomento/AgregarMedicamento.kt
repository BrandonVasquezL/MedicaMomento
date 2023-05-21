package com.example.medicamomento

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


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
        et_Hora.setText("$time")
    }

    //Calendario
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datPicker")
    }
    fun onDateSelected(day: Int, month:Int, year: Int){
        et_Fecha.setText("$day/$month/$year")
    }

/*foto*/
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val imageBitmapa = intent?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.btnCamara)
            imageView.setImageBitmap(imageBitmapa)
        }
    }

    /*botones*/
    fun CancelarCreacion(view: View){
        val intent = Intent(this, SelMedicina::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
        }
        startActivity(intent)
    }
    fun GuardarMedicamento(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
            et_Medicamento = findViewById(R.id.etxt_medicamento)
            et_Dosis = findViewById(R.id.etxt_dosis)
            et_Fecha = findViewById(R.id.etxt_fecha)
            et_Hora = findViewById(R.id.etxt_hora)
            sp_cant = findViewById(R.id.spinner)
            cant = sp_cant.selectedItem.toString()

            val dbHelper = DBhelper(applicationContext)
            val db = dbHelper.writableDatabase

            medicamento = et_Medicamento.text.toString()
            dosis = et_Dosis.text.toString() + " " +cant
            fecha = et_Fecha.text.toString()
            hora = et_Hora.text.toString()


            val values = ContentValues().apply {
                put(Constants.medicinas.COLUMN_MEDICAMENTO, medicamento)
                put(Constants.medicinas.COLUMN_DOSIS, dosis)
                put(Constants.medicinas.COLUMN_FECHA, fecha)
                put(Constants.medicinas.COLUMN_HORARIO, hora)
            }
            val newRowId = db.insert(Constants.medicinas.TABLE_NAME, null, values)
            val idc = newRowId.toString()

            Toast.makeText(applicationContext,idc,Toast.LENGTH_SHORT).show()
        }
        startActivity(intent)
    }
}