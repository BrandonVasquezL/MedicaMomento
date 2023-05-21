package com.example.medicamomento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.medicamomento.fragments.Inicio
import com.example.medicamomento.fragments.Medicamentos
import com.example.medicamomento.fragments.Perfil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.bnInicio)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnInicio -> true
                R.id.bnMedicamentos -> {
                    startActivity(Intent(applicationContext, Medicamentos::class.java))
                    finish()
                    true
                }
                R.id.bnPerfil -> {
                    startActivity(Intent(applicationContext, AgregarPerfil::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

        val dbHelper = DBhelper(applicationContext)
        val db = dbHelper.writableDatabase
        val cursor = db.query(Constants.medicinas.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val medicinas = ArrayList<String>()
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val medicina = cursor.getString(cursor.getColumnIndexOrThrow(Constants.medicinas.COLUMN_MEDICAMENTO))
                val dosis = cursor.getString(cursor.getColumnIndexOrThrow(Constants.medicinas.COLUMN_DOSIS))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow(Constants.medicinas.COLUMN_FECHA))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow(Constants.medicinas.COLUMN_HORARIO))
                val medicamento = "$id $medicina $dosis $fecha $hora"
                medicinas.add(medicamento)
            }

        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            medicinas
        )

        val list_med = findViewById<ListView>(R.id.listv_medicinas)
        list_med.adapter = arrayAdapter

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        //bottom nav
       /*val inicio = Inicio()
        val medicamentos = Medicamentos()
        val perfil = Perfil()

        makeCurrentFragment(inicio)
        

        bottom_Navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.bnInicio -> makeCurrentFragment(inicio)
                R.id.bnMedicamentos -> makeCurrentFragment(medicamentos)
                R.id.bnPerfil -> makeCurrentFragment(perfil)
            }
            true
        }*/
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_supervisado -> Toast.makeText(this, "Modo supervisado", Toast.LENGTH_SHORT).show()
            R.id.nav_supervisor -> Toast.makeText(this, "Modo supervisor", Toast.LENGTH_SHORT).show()
            R.id.nav_registro -> Toast.makeText(this, "ver registros", Toast.LENGTH_SHORT).show()
            R.id.nav_comentario -> Toast.makeText(this, "crear comentario", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}