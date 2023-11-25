package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.android.material.bottomnavigation.BottomNavigationView

class Inicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

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
        agregarMedicamento.setOnClickListener {
            startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
        }
        agregarComentario.setOnClickListener {
            startActivity(Intent(applicationContext, Comentarios::class.java))
        }
    }
}