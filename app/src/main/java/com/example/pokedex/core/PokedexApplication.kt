package com.example.pokedex.core

import android.app.Application
import com.example.pokedex.data.persistence.ObjectBox
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}