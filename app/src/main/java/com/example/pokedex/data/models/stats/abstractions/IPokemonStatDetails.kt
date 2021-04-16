package com.example.pokedex.data.models.stats.abstractions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface IPokemonStatDetails {
    fun getName(context: Context) : String
    @ColorInt fun getColor(context: Context) : Int
}