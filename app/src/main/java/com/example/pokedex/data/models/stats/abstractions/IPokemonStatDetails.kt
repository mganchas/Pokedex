package com.example.pokedex.data.models.stats.abstractions

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface IPokemonStatDetails {
    @StringRes fun getName() : Int
    @ColorRes fun getColor() : Int
}