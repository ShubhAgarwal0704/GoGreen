package com.example.gogreen

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase

class myapplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase or any other global setup here
        FirebaseApp.initializeApp(this)
    }
}