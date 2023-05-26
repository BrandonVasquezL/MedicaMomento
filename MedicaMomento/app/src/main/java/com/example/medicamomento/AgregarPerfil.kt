package com.example.medicamomento

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.medicamomento.databinding.ActivityAgregarPerfilBinding
import com.example.medicamomento.fragments.Medicamentos
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
        binding.btnGuardar.setOnClickListener {
            count += 1
            checkCounter()
        }

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


                val consulta = "SELECT NOMBRE FROM PERFIL"
                val cursor = db.rawQuery(consulta, null)
                if (cursor.moveToFirst()) {
                    resultado= cursor.getString(0)
                }
                cursor.close()
                db.close()


            } else {
                Toast.makeText(
                    this,
                    "Por favor, completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if(resultado === null){
            btnguardar.visibility= View.GONE
            btneditar.visibility=View.VISIBLE
            btnguardar2.visibility=View.VISIBLE
            btnguardar2.isEnabled = true
            nombre.isEnabled = false
            edad.isEnabled = false
            sangre.isEnabled = false
            enfermedades.isEnabled = false
            alergias.isEnabled = false
            servicio.isEnabled = false


            val consulta = "SELECT NOMBRE FROM PERFIL"


            val cursor = db.rawQuery(consulta, null)


            if (cursor.moveToFirst()) {
                resultado= cursor.getString(0)
            }
            cursor.close()
            db.close()
            nombre.setText(resultado)
        }

        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.bnPerfil
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnPerfil-> true
                R.id.bnMedicamentos -> {
                    startActivity(Intent(applicationContext, Medicamentos::class.java))
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
        if(count == 2){
            showAds()
            count = 0
            initAds()
        }
    }

    private fun showAds(){
        interstitial?.show(this)
    }
}