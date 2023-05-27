package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.android.material.bottomnavigation.BottomNavigationView

class Inicio : AppCompatActivity() {

    private var chart: AnyChartView? = null

    private val avanceT = listOf(200,300,400,600)
    private val tratamiento = listOf("Paracetamol", "Levotiroxina", "Salbutamol", "Naproxeno")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        chart = findViewById(R.id.pieChart)

        configChartView()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
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

    }
        private fun configChartView(){
            val pie : Pie = AnyChart.pie()
            val dataPieChart: MutableList<DataEntry> = mutableListOf()

            for(index in avanceT.indices){
                dataPieChart.add(ValueDataEntry(tratamiento.elementAt(index), avanceT.elementAt(index)))
            }
            pie.data(dataPieChart)
            pie.title("Avance Tratamiento")
            chart!!.setChart(pie)
        }
}