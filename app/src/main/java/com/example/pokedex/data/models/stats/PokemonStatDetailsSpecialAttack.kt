package com.example.pokedex.data.models.stats

import android.util.Log
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails

class PokemonStatDetailsSpecialAttack : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsSpecialAttack::class.java.simpleName
    }

    override fun getName() : Int {
        Log.d(TAG, "getName()")
        TODO("Not yet implemented")
    }

    override fun getColor(): Int {
        Log.d(TAG, "getColor()")
        TODO("Not yet implemented")
    }
}