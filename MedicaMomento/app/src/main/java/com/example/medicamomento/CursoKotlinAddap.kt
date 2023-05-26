package com.example.medicamomento

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.MobileAds

class CursoKotlinAddap : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}