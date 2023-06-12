package com.example.medicamomento

<<<<<<< HEAD
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
=======

>>>>>>> 20e129659431f39c0cc3ab106461a100529383b3
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
<<<<<<< HEAD
=======
import android.view.View
import android.widget.Button
>>>>>>> 20e129659431f39c0cc3ab106461a100529383b3
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
<<<<<<< HEAD

=======
>>>>>>> 20e129659431f39c0cc3ab106461a100529383b3
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter: Adaptador
    private lateinit var adaptador2: Adaptador2
    private var isSecondRecyclerViewVisible = false
    private lateinit var medicamentos: List<DBhelper.Medicamento>







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
                    //Despues de autenticar, debe mostrar la activity de supervisado






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
        setContentView(R.layout.activity_main)



        //boton agregar medicamentos
        val btnMasmedic : FloatingActionButton = findViewById(R.id.btnMedicamento)
        btnMasmedic.imageTintList = ColorStateList.valueOf(Color.WHITE)
        btnMasmedic.setOnClickListener {
            startActivity(Intent(applicationContext, AgregarMedicamento::class.java))
        }
        //bottomnav
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
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

        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView2.visibility = View.VISIBLE
        recyclerView2.layoutManager = LinearLayoutManager(this)
        adaptador2 = Adaptador2(medicamentos)
        recyclerView2.adapter = adaptador2




        val suspender: Button = findViewById(R.id.btnSuspender)
        val editar: Button = findViewById(R.id.btnEditar)
        editar.isEnabled = false
        suspender.isEnabled= false


        editar.setOnClickListener {
            // Actualizar la posición seleccionada en el adaptador
            val selectedItemPosition = adapter.getSelectedPosition()
            adapter.updateSelectedPosition(selectedItemPosition)

            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                val medicamento = adapter.getMedicamentoAtPosition(selectedItemPosition)

                medicamento?.let {
                    val intent = Intent(this, EditarMedicina::class.java)
                    intent.putExtra("id", it.id)
                    intent.putExtra("medicamento", it.nombre)
                    intent.putExtra("dosis", it.dosis)
                    intent.putExtra("fecha", it.fecha)
                    intent.putExtra("hora", it.horario)
                    // Agrega los demás datos del medicamento que desees editar
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Selecciona un medicamento", Toast.LENGTH_SHORT).show()
            }
        }

        if (adapter.itemCount > 0) {
            editar.isEnabled = true
            suspender.isEnabled = true
        }






        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

 fun ocultarAgregarBtn(bnMasMedicamento: AgregarMedicamento){
    bnMasMedicamento.setVisible(false)
}




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_supervisado ->

                startActivity(Intent(applicationContext,modoSupervisado::class.java))
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