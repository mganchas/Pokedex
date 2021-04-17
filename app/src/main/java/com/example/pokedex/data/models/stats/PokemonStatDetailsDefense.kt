package com.example.pokedex.data.models.stats

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pokedex.R
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails

class PokemonStatDetailsDefense : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsDefense::class.java.simpleName
    }

    override fun getName(context: Context) : String {
        Log.d(TAG, "getName()")
        return context.getString(R.string.stat_defense)
    }

    override fun getColor(context: Context): Int {
        Log.d(TAG, "getColor()")
        return ContextCompat.getColor(context, R.color.defense)
    }
}