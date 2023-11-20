package com.example.medicamomento

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import com.example.medicamomento.databinding.ActivityComentariosBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class Comentarios : AppCompatActivity(),TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityComentariosBinding
    private lateinit var textToSpeech: TextToSpeech
    private var isVoiceInstructionsCompleted = false
    private var voiceInstructionsPlayed = false
    private var isTTSInitialized = false
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
            val instructions = "Describame sus sintomas y apareceran en pantalla"

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
        }, 3000) // Espera 3 segundos, ajusta según sea necesario
    }

    // Este método se llama cuando el reconocimiento de voz se completa
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val editxt:EditText=findViewById(R.id.txtCoemntario)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            //Muestra el texto reconocido por la voz en el EditText
            editxt.setText(spokenText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtcomentario:EditText = findViewById(R.id.txtCoemntario)
        val btncoment:Button = findViewById(R.id.btn_coments)
        val btnvoz:FloatingActionButton= findViewById(R.id.comentariosvoz)

        val dbHelper = DBhelper(applicationContext)
        val db = dbHelper.writableDatabase

        initializeTextToSpeech()
        btnvoz.setOnClickListener {
            speakAndThenRecognize()
        }
        btncoment.setOnClickListener {
            val comment = txtcomentario.text.toString()
            if (comment.isNotEmpty()){

                val values = ContentValues().apply {
                    put(Constants.comentarios.COLUMN_COMENT, comment)
                }
                db.insert(Constants.comentarios.TABLE_NAME, null, values)
                Toast.makeText(applicationContext,"Comentario guardado",Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Comentarios::class.java))
                overridePendingTransition(0, 0)

            }else{
                Toast.makeText(
                    this,
                    "Primero llena un comentario",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val cursor = db.query(Constants.comentarios.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val comentarios = ArrayList<String>()
        while (cursor.moveToNext()) {
            val coment = cursor.getString(cursor.getColumnIndexOrThrow(Constants.comentarios.COLUMN_COMENT))
            val comentada = "$coment"
            comentarios.add(comentada)
        }

        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            comentarios
        )

        val list_com = findViewById<ListView>(R.id.listv_comentarios)
        list_com.adapter = arrayAdapter

    }
    fun regresar(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Regreso")
        }
        startActivity(intent)
    }
}