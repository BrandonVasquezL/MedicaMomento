package com.example.medicamomento

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.medicamomento.databinding.Fragment4Binding

private var _binding: Fragment4Binding? = null
private val binding get() = _binding!!
private val DURATION: Long = 2000
class Fragment4 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment4Binding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener{
            cambiarActivity()
        }
        return binding.root
    }
    private fun cambiarActivity() {
        val dbHelper = DBhelper(requireContext())
        val db = dbHelper.writableDatabase

        val consulta = "SELECT Medicamento FROM Mis_Medicamentos"
        val cursor = db.rawQuery(consulta, null)

        if (cursor.moveToFirst()) {
            if (cursor.count > 0) {
                // Si ya hay registros en la base de datos, redirige a MainActivity
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            } else {
                // Si no hay registros en la base de datos, redirige a SelMedicina
                val intent = Intent(requireActivity(), SelMedicina::class.java)
                startActivity(intent)
            }
        }

        cursor.close()
        db.close()
        dbHelper.close()
    }
}