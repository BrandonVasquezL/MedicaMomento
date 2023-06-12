package com.example.medicamomento

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.medicamomento.databinding.ActivityAgregarPerfilBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView

class AgregarPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarPerfilBinding
    private var interstitial: InterstitialAd? = null
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAds()
        initListeners()

        val nombre:EditText= findViewById(R.id.etname)
        val edad:EditText = findViewById(R.id.edEdad)
        val sangre:EditText = findViewById(R.id.edSangre)
        val enfermedades:EditText = findViewById(R.id.edEnfermedades)
        val alergias:EditText = findViewById(R.id.edAlergias)
        val servicio:EditText = findViewById(R.id.edServicio)
        val btnguardar:Button = findViewById(R.id.agregarP)
        val btnguardar2:Button = findViewById(R.id.btnGuardar)
        val btneditar:Button = findViewById(R.id.btnEditar)
        var resultado: String? = null
        val dbHelper = DBhelper(applicationContext)
        val db = dbHelper.writableDatabase

        btnguardar.setOnClickListener{
            val name = nombre.text.toString()
            val edad2 = edad.text.toString()
            val sangre2 = sangre.text.toString()
            val enfermedades2 = enfermedades.text.toString()
            val alergias2 = alergias.text.toString()
            val servicio2 = servicio.text.toString()



            if (name.isNotEmpty() && edad2.isNotEmpty() && sangre2.isNotEmpty()) {

                val values = ContentValues().apply {
                    put(Constants.perfil.COLUMN_NOMBRE, name)
                    put(Constants.perfil.COLUMN_EDAD, edad2)
                    put(Constants.perfil.COLUMN_SANGRE, sangre2)
                    put(Constants.perfil.COLUMN_ENFERMEDADES, enfermedades2)
                    put(Constants.perfil.COLUMN_ALERGIAS, alergias2)
                    put(Constants.perfil.COLUMN_SERVICIO, servicio2)
                }
                db.insert(Constants.perfil.TABLE_NAME, null, values)
                Toast.makeText(applicationContext,"Datos guardados",Toast.LENGTH_SHORT).show()

                btnguardar.visibility= View.GONE
                btneditar.visibility=View.VISIBLE
                btnguardar2.visibility=View.VISIBLE
                btnguardar2.isEnabled = false
                nombre.isEnabled = false
                edad.isEnabled = false
                sangre.isEnabled = false
                enfermedades.isEnabled = false
                alergias.isEnabled = false
                servicio.isEnabled = false

            } else {
                Toast.makeText(
                    this,
                    "Por favor, completa nombre, edad y tipo de sangre los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val consulta = "SELECT NOMBRE FROM PERFIL"
        val cursor = db.rawQuery(consulta, null)
        fun validar(): Boolean {
            return cursor.count > 0
            cursor.close()
            db.close()
        }

        if(validar() === true){
            // Obtiene el perfil de la base de datos
            val dbHelper = DBhelper(applicationContext)
            val perfil = dbHelper.getPerfil().first()

            // Guarda el perfil en SharedPreferences
            guardarPerfilEnSharedPreferences(perfil)

            btnguardar.visibility= View.GONE
            btneditar.visibility=View.VISIBLE
            btnguardar2.visibility=View.VISIBLE
            btnguardar2.isEnabled = false
            nombre.isEnabled = false
            edad.isEnabled = false
            sangre.isEnabled = false
            enfermedades.isEnabled = false
            alergias.isEnabled = false
            servicio.isEnabled = false

            val consulta1 = "SELECT NOMBRE FROM PERFIL"
            val consulta2 = "SELECT EDAD FROM PERFIL"
            val consulta3 = "SELECT T_Sangre FROM PERFIL"
            val consulta4 = "SELECT ENFERMEDADES FROM PERFIL"
            val consulta5 = "SELECT ALERGIAS FROM PERFIL"
            val consulta6 = "SELECT S_MEDICO FROM PERFIL"

            val cursor1 = db.rawQuery(consulta1, null)
            val cursor2 = db.rawQuery(consulta2, null)
            val cursor3 = db.rawQuery(consulta3, null)
            val cursor4 = db.rawQuery(consulta4, null)
            val cursor5 = db.rawQuery(consulta5, null)
            val cursor6 = db.rawQuery(consulta6, null)

            var resultado1 = ""
            var resultado2 = ""
            var resultado3 = ""
            var resultado4 = ""
            var resultado5 = ""
            var resultado6 = ""

            if (cursor1.moveToFirst()) {
                resultado1 = cursor1.getString(0)
            }
            if (cursor2.moveToFirst()) {
                resultado2 = cursor2.getString(0)
            }
            if (cursor3.moveToFirst()) {
                resultado3 = cursor3.getString(0)
            }
            if (cursor4.moveToFirst()) {
                resultado4 = cursor4.getString(0)
            }
            if (cursor5.moveToFirst()) {
                resultado5 = cursor5.getString(0)
            }
            if (cursor6.moveToFirst()) {
                resultado6 = cursor6.getString(0)
            }

            cursor1.close()
            cursor2.close()
            cursor3.close()
            cursor4.close()
            cursor5.close()
            cursor6.close()

            db.close()

            nombre.setText(resultado1)
            edad.setText(resultado2)
            sangre.setText(resultado3)
            enfermedades.setText(resultado4)
            alergias.setText(resultado5)
            servicio.setText(resultado6)

        }

        //llamada a los anuncios y editar perfil
        binding.btnEditar.setOnClickListener {
            count += 1
            checkCounter()
            btneditar.isEnabled = false
            btnguardar2.isEnabled = true
            nombre.isEnabled = true
            edad.isEnabled = true
            sangre.isEnabled = true
            enfermedades.isEnabled = true
            alergias.isEnabled = true
            servicio.isEnabled = true

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val perfilId = sharedPreferences.getInt("perfil_id", 0)
            val perfilNombre = sharedPreferences.getString("perfil_nombre", "")
            val perfilEdad = sharedPreferences.getInt("perfil_edad", 0)
            val perfilSangre = sharedPreferences.getString("perfil_sangre", "")
            val perfilEnfermedades = sharedPreferences.getString("perfil_enfermedades", "")
            val perfilAlergias = sharedPreferences.getString("perfil_alergias", "")
            val perfilServicio = sharedPreferences.getString("perfil_servicio", "")

            // Establece los datos del perfil en los campos de texto
            nombre.setText(perfilNombre)
            edad.setText(perfilEdad.toString())
            sangre.setText(perfilSangre)
            enfermedades.setText(perfilEnfermedades)
            alergias.setText(perfilAlergias)
            servicio.setText(perfilServicio)
        }
        binding.btnGuardar.setOnClickListener { btneditar.isEnabled = true

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val perfilId = sharedPreferences.getInt("perfil_id", 0)
            val perfilNombre = nombre.text.toString()
            val perfilEdad = edad.text.toString().toInt()
            val perfilSangre = sangre.text.toString()
            val perfilEnfermedades = enfermedades.text.toString()
            val perfilAlergias = alergias.text.toString()
            val perfilServicio = servicio.text.toString()
            val edadString = edad.text.toString()
            if (perfilNombre.isNotEmpty() && edadString.isNotEmpty() && perfilSangre.isNotEmpty()) {
            // Actualiza los datos del perfil en SharedPreferences
            val perfilActualizado = DBhelper.Perfil(
                perfilId,
                perfilNombre,
                perfilEdad,
                perfilSangre,
                perfilEnfermedades,
                perfilAlergias,
                perfilServicio
            )
            guardarPerfilEnSharedPreferences(perfilActualizado)
            // Actualiza los datos del perfil en la base de datos SQLite
            val dbHelper = DBhelper(applicationContext)
            dbHelper.actualizarPerfil(perfilActualizado)
                btnguardar2.isEnabled = false
                nombre.isEnabled = false
                edad.isEnabled = false
                sangre.isEnabled = false
                enfermedades.isEnabled = false
                alergias.isEnabled = false
                servicio.isEnabled = false
            }else {
                Toast.makeText(
                    this,
                    "Por favor, completa nombre, edad y tipo de sangre los campos",
                    Toast.LENGTH_SHORT
                ).show()
                btneditar.isEnabled = false
                btnguardar2.isEnabled = true
                nombre.isEnabled = true
                edad.isEnabled = true
                sangre.isEnabled = true
                enfermedades.isEnabled = true
                alergias.isEnabled = true
                servicio.isEnabled = true
            }
        }

        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation2)
        bottomNavigationView.selectedItemId = R.id.bnPerfil
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnPerfil-> true
                R.id.bnMedicamentos -> {
                    startActivity(Intent(applicationContext, Inicio::class.java))
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
    }
    private fun guardarPerfilEnSharedPreferences(perfil: DBhelper.Perfil) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("perfil_id", perfil.id)
        editor.putString("perfil_nombre", perfil.nombre)
        editor.putInt("perfil_edad", perfil.edad)
        editor.putString("perfil_sangre", perfil.sangre)
        editor.putString("perfil_enfermedades", perfil.enfermedades)
        editor.putString("perfil_alergias", perfil.alergias)
        editor.putString("perfil_servicio", perfil.servicio)
        editor.apply()
    }


    private fun initListeners() {
        interstitial?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                interstitial = null
            }
        }
    }

    private fun initAds() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitial = interstitialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitial = null
            }
        })
    }

    private fun checkCounter() {
        if(count == 1){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(this)
    }
}