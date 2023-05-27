package com.example.medicamomento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie

class Inicio : AppCompatActivity() {

    private var chart: AnyChartView? = null

    private val avanceT = listOf(200,300,400,600)
    private val tratamiento = listOf("Paracetamol", "Levotiroxina", "Salbutamol", "Naproxeno")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        chart = findViewById(R.id.pieChart)

        configChartView()

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