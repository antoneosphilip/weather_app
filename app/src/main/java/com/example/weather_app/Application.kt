package com.example.weather_app


import android.app.Application
import androidx.work.Configuration

class MyApplication:Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer=AppContainerImpl(this)
    }

}