package com.example.medicamomento

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.provider.BaseColumns
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class EditarMedicina : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var edNombre: TextView
    private lateinit var edDosis: TextView
    private lateinit var edFecha: TextView
    private lateinit var edHora: TextView
    private lateinit var btncancelar: Button
    private lateinit var btnGuardar: Button
    private lateinit var textToSpeech: TextToSpeech
    private var isVoiceInstructionsCompleted = false
    private var voiceInstructionsPlayed = false
    private var isTTSInitialized = false
    private lateinit var comandovoz: FloatingActionButton

    var medicamento: String? = null
    var dosis: String? = null
    var fecha: String? = null
    var hora: String? = null

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
            val instructions = "Diga el nombre del medicamento y los numeros que usara en la dosis para poder editarlos"

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
        }, 7000) // Espera 3 segundos, ajusta según sea necesario
    }

    // Este método se llama cuando el reconocimiento de voz se completa
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            //Muestra el texto reconocido por la voz en el EditText
            edNombre=findViewById(R.id.edNombre)
            edDosis=findViewById(R.id.edDosis)
            // Divide el texto en palabras
            val palabras = spokenText?.split("\\s+".toRegex())

            // Itera sobre las palabras y determina si cada una es un número
            var contieneNumeros = false
            var dosis = ""
            var medicamento = ""

            palabras?.forEach { palabra ->
                if (palabra.matches(Regex("\\d+"))) {
                    // Si la palabra es un número, reemplaza el número en la dosis
                    val dosisActual = edDosis.text.toString()
                    // Busca y reemplaza el número existente en la dosis
                    val dosisSinNumero = dosisActual.replaceFirst(Regex("\\d+"), "")
                    dosis = "$palabra$dosisSinNumero"
                    contieneNumeros = true
                } else {
                    // Si la palabra no es un número, agrégala al medicamento
                    medicamento += "$palabra "
                }
            }


            // Asigna el texto a los EditText correspondientes
            edNombre.setText(medicamento.trim())
            edDosis.setText(dosis.trim())
        }
    }
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
        comandovoz=findViewById(R.id.editarvoz)
        initializeTextToSpeech()
        comandovoz.setOnClickListener {
            speakAndThenRecognize()
        }
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

