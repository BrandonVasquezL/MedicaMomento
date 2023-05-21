package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicamomento.fragments.Medicamentos
import com.google.android.material.bottomnavigation.BottomNavigationView

class AgregarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)

        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.bnPerfil)
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
}