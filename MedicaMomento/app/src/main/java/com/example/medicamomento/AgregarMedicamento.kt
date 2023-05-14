package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View

class AgregarMedicamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_medicamento)
    }
    fun CancelarCreacion(view: View){
        val intent = Intent(this, SelMedicina::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
        }
        startActivity(intent)
    }
    fun GuardarMedicamento(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Creacion de medicamento")
        }
        startActivity(intent)
    }
}