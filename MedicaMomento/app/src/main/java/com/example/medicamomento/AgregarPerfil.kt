package com.example.medicamomento

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.medicamomento.fragments.Medicamentos
import com.google.android.material.bottomnavigation.BottomNavigationView

class AgregarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)

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
        var ban = 0

        btnguardar.setOnClickListener(){
            val name = nombre.text.toString()
            val edad2 = edad.text.toString()
            val sangre2 = sangre.text.toString()
            val enfermedades2 = enfermedades.text.toString()
            val alergias2 = alergias.text.toString()
            val servicio2 = servicio.text.toString()



            if (name.isNotEmpty() && edad2.isNotEmpty() && sangre2.isNotEmpty()) {
                val user = User(name, edad2, sangre2, enfermedades2, alergias2, servicio2)

                val values = ContentValues().apply {
                    put(Constants.perfil.COLUMN_NOMBRE, name)
                    put(Constants.perfil.COLUMN_EDAD, edad2)
                    put(Constants.perfil.COLUMN_SANGRE, sangre2)
                    put(Constants.perfil.COLUMN_ENFERMEDADES, enfermedades2)
                    put(Constants.perfil.COLUMN_ALERGIAS, alergias2)
                    put(Constants.perfil.COLUMN_SERVICIO, servicio2)
                }
                val newRowId = db.insert(Constants.perfil.TABLE_NAME, null, values)
                Toast.makeText(applicationContext,"Datos guardados",Toast.LENGTH_SHORT).show()
                btnguardar.visibility= View.GONE

                btneditar.visibility=View.VISIBLE
                btnguardar2.visibility=View.VISIBLE


                val consulta = "SELECT NOMBRE FROM PERFIL"
                val cursor = db.rawQuery(consulta, null)
                if (cursor.moveToFirst()) {
                    resultado= cursor.getString(0)
                     ban = 1
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

        if(ban == 1){
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