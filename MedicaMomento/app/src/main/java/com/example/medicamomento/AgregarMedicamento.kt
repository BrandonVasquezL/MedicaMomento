package com.example.medicamomento

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class AgregarMedicamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_medicamento)

        val btnCamara = findViewById<ImageView>(R.id.btnCamara)
        btnCamara.setOnClickListener{
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val imageBitmapa = intent?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.btnCamara)
            imageView.setImageBitmap(imageBitmapa)
        }
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