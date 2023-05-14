package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View

class SelMedicina : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sel_medicina)
    }
    fun CrearMedicamento(view: View){
        val intent = Intent(this, AgregarMedicamento::class.java).apply {
            putExtra(EXTRA_MESSAGE, "Creacion de medicamento")
        }
        startActivity(intent)
    }
}