package com.example.medicamomento

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.provider.BaseColumns
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.util.Locale


class AgregarMedicamento : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var et_Medicamento: EditText
    private lateinit var et_Dosis: EditText
    private lateinit var et_Fecha: EditText
    private lateinit var et_Hora: EditText
    private lateinit var sp_cant: Spinner
    private lateinit var textToSpeech: TextToSpeech
    private var isVoiceInstructionsCompleted = false
    private var voiceInstructionsPlayed = false
    private var isTTSInitialized = false
    private lateinit var comandovoz:FloatingActionButton
    var medicamento: String? = null
    var dosis: String? = null
    var fecha: String? = null
    var hora: String? = null
    var cant: String? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language not supported")
            }else{
                isVoiceInstructionsCompleted = true
                isTTSInitialized=true
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed")
        }
    }
    private fun initializeTextToSpeech() {
        if (!isTTSInitialized) {
            textToSpeech = TextToSpeech(this, this)
        }
    }
    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
        startActivityForResult(intent, 1)
    }
    private fun speakAndThenRecognize() {
        if (!voiceInstructionsPlayed) {
            val instructions = "Diga el nombre del medicamento y los numeros que usara en la dosis"

            // Reproducir las instrucciones en voz
            textToSpeech.speak(instructions, TextToSpeech.QUEUE_FLUSH, null, null)

            // Marcar las instrucciones de voz como reproducidas
            voiceInstructionsPlayed = true
        }

        // Iniciar temporizador para esperar antes de iniciar el reconocimiento de voz
        Handler().postDelayed({
            // Verificar si las instrucciones de voz se han completado
            if (isVoiceInstructionsCompleted) {
                // Iniciar el reconocimiento de voz después del temporizador
                startSpeechRecognition()
            }
            voiceInstructionsPlayed=false
        }, 5000) // Espera 3 segundos, ajusta según sea necesario
    }

    // Este método se llama cuando el reconocimiento de voz se completa
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            //Muestra el texto reconocido por la voz en el EditText
            et_Medicamento=findViewById(R.id.etxt_medicamento)
            et_Dosis=findViewById(R.id.etxt_dosis)
            // Divide el texto en palabras
            val palabras = spokenText?.split("\\s+".toRegex())

            // Itera sobre las palabras y determina si cada una es un número
            var contieneNumeros = false
            var dosis = ""
            var medicamento = ""

            palabras?.forEach { palabra ->
                if (palabra.matches(Regex("\\d+"))) {
                    // Si la palabra es un número, agrégala a la dosis
                    dosis += "$palabra "
                    contieneNumeros = true
                } else {
                    // Si la palabra no es un número, agrégala al medicamento
                    medicamento += "$palabra "
                }
            }

            // Asigna el texto a los EditText correspondientes
            et_Medicamento.setText(medicamento.trim())
            et_Dosis.setText(dosis.trim())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_medicamento)
        et_Fecha = findViewById(R.id.etxt_fecha)
        et_Fecha.setOnClickListener { showDatePickerDialog() }

        et_Hora = findViewById(R.id.etxt_hora)
        et_Hora.setOnClickListener { showTimePickerDialog() }

        comandovoz=findViewById(R.id.agregarvoz)
        initializeTextToSpeech()
        comandovoz.setOnClickListener {
            speakAndThenRecognize()
        }

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