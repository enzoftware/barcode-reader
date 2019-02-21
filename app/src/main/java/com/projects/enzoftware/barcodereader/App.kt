package com.projects.enzoftware.barcodereader

import android.app.Application
import com.google.firebase.FirebaseApp

class App : Application() {
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }
}