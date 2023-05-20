package com.example.medicamomento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicamomento.databinding.ActivityTutorialBinding
import com.google.android.material.tabs.TabLayoutMediator


class tutorial : AppCompatActivity() {

    private  lateinit var binding: ActivityTutorialBinding
    private val adapter by lazy { ViewPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pager.adapter=adapter
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout,binding.pager){tab,position ->}.attach()
    }

}