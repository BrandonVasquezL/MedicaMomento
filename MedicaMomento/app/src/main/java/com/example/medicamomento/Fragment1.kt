package com.example.medicamomento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medicamomento.databinding.Fragment1Binding


class Fragment1 : Fragment() {

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_1, container, false)
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }


}