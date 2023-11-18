/*package com.example.medicamomento

class ModoSupervisado {
}*/
package com.example.medicamomento

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class ModoSupervisado1 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adaptador
    private lateinit var medicamentos: List<DBhelper.Medicamento>

    //Instanciar clase
    lateinit var agregarPerfil: AgregarPerfil
    lateinit var mainActivity: MainActivity




    //Implementacion de autenticacion biometrica

    private var cancellationSignal: android.os.CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notificarUsuario("Error de autenticacion: $errString")
                }

                override  fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?){
                    super.onAuthenticationSucceeded(result)
                    notificarUsuario("Autenticacion completada")
                    (Intent(this@ModoSupervisado1,MainActivity::class.java))
                    finish()




                }

            }



    private fun notificarUsuario(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun autenticarSupervisor(){
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Autenticar supervisor")
            .setSubtitle("Se requiere autenticación")
            .setDescription("Para cambiar al modo supervisor, coloque su dedo en el lector")
            .setNegativeButton("Cancelar",this.mainExecutor,
                DialogInterface.OnClickListener{ dialog, which->
                    notificarUsuario("Autenticacion cancelada")
                }).build()

        biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallback)


    }




    private fun getCancellationSignal(): android.os.CancellationSignal{
        cancellationSignal = android.os.CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notificarUsuario("La autenticacion fue cancelada por el usuario")
        }
        return cancellationSignal as android.os.CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            notificarUsuario("Autenticacion de huella no esta activada en configuracion")
            return false
        }
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            notificarUsuario("Autenticacion de huella digital no esta habilitada")
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }
        else true
    }



    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisado)
        /*
                //boton agregar medicamentos
                val btnMasmedic : FloatingActionButton = findViewById(R.id.btnMedicamento)
                btnMasmedic.imageTintList = ColorStateList.valueOf(Color.WHITE)
                btnMasmedic.setOnClickListener {
                    startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
                }

          */


        //corregir para que se muestre unicamente las vistas de supervisado
        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation2)
        bottomNavigationView.selectedItemId = R.id.bnInicio
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnInicio -> true
                R.id.bnMedicamentos -> {
                    startActivity(Intent(applicationContext, Inicio::class.java))
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

        val database = DBhelper.getInstance(this)
        medicamentos = database.getMedicamentos()

        // Configurar el RecyclerView y el adaptador
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adaptador(medicamentos)
        recyclerView.adapter = adapter





        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)


        /*val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)*/

        val navigationView = findViewById<NavigationView>(R.id.nav_view2)
        navigationView.setNavigationItemSelectedListener(this)
        /*val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_supervisado -> {
                agregarPerfil.ocultarBotones()
            }
            R.id.nav_supervisor ->
                autenticarSupervisor()

            R.id.nav_registro -> Toast.makeText(this, "ver registros", Toast.LENGTH_SHORT).show()
            R.id.nav_comentario -> startActivity(Intent(applicationContext, Comentarios::class.java))
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