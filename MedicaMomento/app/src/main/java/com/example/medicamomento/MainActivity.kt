package com.example.medicamomento


import android.app.Activity


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.Locale

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, TextToSpeech.OnInitListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter: Adaptador
    private lateinit var adaptador2: Adaptador2
    private lateinit var medicamentos: List<DBhelper.Medicamento>
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
            val instructions = "Hola usuario, recuerda que los comandos estan en el apartado de inicio "

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
                "editar medicamento" -> {
                    val intent = Intent(applicationContext, EditarMedicina::class.java)
                    // Aquí deberías obtener los datos del medicamento seleccionado y pasarlo al Intent
                    val selectedItemPosition = adapter.getSelectedPosition()
                    if (selectedItemPosition != RecyclerView.NO_POSITION) {
                        val medicamento = adapter.getMedicamentoAtPosition(selectedItemPosition)
                        intent.putExtra("id", medicamento?.id)
                        intent.putExtra("medicamento", medicamento?.nombre)
                        intent.putExtra("dosis", medicamento?.dosis)
                        intent.putExtra("fecha", medicamento?.fecha)
                        intent.putExtra("hora", medicamento?.horario)
                        // Agrega los demás datos del medicamento que desees editar
                    }
                    startActivity(intent)
                }
                "comentarios" -> startActivity(Intent(applicationContext, Comentarios::class.java))
                else -> {
                    // Aquí puedes manejar otras frases reconocidas o mostrar un mensaje de error
                    // Por ejemplo, mostrar un Toast indicando que la frase no fue reconocida
                    Toast.makeText(this, "Frase no reconocida", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicializar instruccion de voz
        initializeTextToSpeech()
        //Boton agregar medicamentos sin voz
        val btnAgregarMed:Button = findViewById(R.id.btnAgregarMedicamento)

        //boton agregar medicamentos
        val btnMasmedic : FloatingActionButton = findViewById(R.id.btnMedicamento)
        btnMasmedic.imageTintList = ColorStateList.valueOf(Color.WHITE)
        btnAgregarMed.setOnClickListener {
            startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
        }
        btnMasmedic.setOnClickListener{
            speakAndThenRecognize()
        }

        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.bnInicio
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnInicio -> true
                R.id.bnMedicamentos -> {
                    startActivity(Intent(applicationContext, Inicio::class.java))
                    finish()
                    true
                }
                R.id.bnPerfil -> {
                    startActivity(Intent(applicationContext, AgregarPerfil::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

        val database = DBhelper.getInstance(this)
        medicamentos = database.getMedicamentos()

        // Configurar el RecyclerView y el adaptador
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adaptador(medicamentos)
        recyclerView.adapter = adapter

        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView2.visibility = View.VISIBLE
        recyclerView2.layoutManager = LinearLayoutManager(this)
        adaptador2 = Adaptador2(medicamentos)
        recyclerView2.adapter = adaptador2


        val suspender: Button = findViewById(R.id.btnAgregarMedicamento)
        val editar: Button = findViewById(R.id.btnEditar)
        editar.isEnabled = false
        suspender.isEnabled= false


        editar.setOnClickListener {
            // Actualizar la posición seleccionada en el adaptador
            val selectedItemPosition = adapter.getSelectedPosition()
            adapter.updateSelectedPosition(selectedItemPosition)

            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                val medicamento = adapter.getMedicamentoAtPosition(selectedItemPosition)

                medicamento?.let {
                    val intent = Intent(this, EditarMedicina::class.java)
                    intent.putExtra("id", it.id)
                    intent.putExtra("medicamento", it.nombre)
                    intent.putExtra("dosis", it.dosis)
                    intent.putExtra("fecha", it.fecha)
                    intent.putExtra("hora", it.horario)
                    // Agrega los demás datos del medicamento que desees editar
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Selecciona un medicamento", Toast.LENGTH_SHORT).show()
            }
        }

        if (adapter.itemCount > 0) {
            editar.isEnabled = true
            suspender.isEnabled = true
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_comentario -> startActivity(Intent(applicationContext, Comentarios::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}