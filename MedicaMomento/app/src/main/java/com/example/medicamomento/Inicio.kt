package com.example.medicamomento

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class Inicio : AppCompatActivity(),TextToSpeech.OnInitListener {
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
            val instructions = "Hola usuario, pruebe uno de los comandos de voz "

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
            // Verificar las diferentes frases reconocidas y realizar acciones correspondientes
            when (spokenText?.toLowerCase(Locale.getDefault())) {
                "agregar medicamento" -> startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
                "comentarios" -> startActivity(Intent(applicationContext, Comentarios::class.java))
                else -> {
                    Toast.makeText(this, "Frase no reconocida", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        //Inicializar instruccion de voz
        initializeTextToSpeech()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation2)
        bottomNavigationView.selectedItemId = R.id.bnMedicamentos
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnMedicamentos-> true
                R.id.bnPerfil -> {
                    startActivity(Intent(applicationContext, AgregarPerfil::class.java))
                    finish()
                    true
                }
                R.id.bnInicio-> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

        val agregarMedicamento: Button = findViewById(R.id.agregar_Medicamento)
        val agregarComentario: Button = findViewById(R.id.agregar_Comentario)
        val reconocimientovoz:FloatingActionButton= findViewById(R.id.pruebavoz)
        agregarMedicamento.setOnClickListener {
            startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
        }
        agregarComentario.setOnClickListener {
            startActivity(Intent(applicationContext, Comentarios::class.java))
        }
        reconocimientovoz.setOnClickListener {
            speakAndThenRecognize()
        }
    }
}