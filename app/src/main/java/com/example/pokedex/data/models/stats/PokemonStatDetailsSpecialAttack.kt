package com.example.pokedex.data.models.stats

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pokedex.R
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails

class PokemonStatDetailsSpecialAttack : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsSpecialAttack::class.java.simpleName
    }

    override fun getName(context: Context) : String {
        Log.d(TAG, "getName()")
        return context.getString(R.string.stat_special_attack)
    }

    override fun getColor(context: Context): Int {
        Log.d(TAG, "getColor()")
        return ContextCompat.getColor(context, R.color.special_attack)
    }
}