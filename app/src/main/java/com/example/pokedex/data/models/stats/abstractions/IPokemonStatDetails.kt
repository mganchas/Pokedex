package com.example.pokedex.data.models.stats.abstractions

import android.content.Context
import android.graphics.drawable.Drawable

interface IPokemonStatDetails {
    fun getName(context: Context) : String
    fun getIcon(context: Context) : Drawable?
    fun getColorLevel() : Int
}