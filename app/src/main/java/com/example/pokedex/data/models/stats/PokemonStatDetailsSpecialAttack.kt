package com.example.pokedex.data.models.stats

import android.content.Context
import android.util.Log
import com.example.pokedex.R
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStats

class PokemonStatDetailsSpecialAttack : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsSpecialAttack::class.java.simpleName
    }

    override fun getName(context: Context) : String {
        Log.d(TAG, "getName()")
        return context.getString(R.string.stat_special_attack)
    }

    override fun getIcon(context: Context): Int {
        Log.d(TAG, "getIcon()")
        return R.drawable.ic_magic
    }

    override fun getColorLevel(context: Context): Int {
        Log.d(TAG, "getColorLevel()")
        return PokemonStats.SpecialAttack.ordinal
    }
}