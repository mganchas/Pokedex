package com.example.pokedex.data.models.stats

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pokedex.R
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStatTypes

class PokemonStatDetailsSpecialDefense : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsSpecialDefense::class.java.simpleName
    }

    override fun getName(context: Context) : String {
        Log.d(TAG, "getName()")
        return context.getString(R.string.stat_special_defense)
    }

    override fun getIcon(context: Context): Drawable? {
        Log.d(TAG, "getIcon()")
        return ContextCompat.getDrawable(context, R.drawable.ic_wall)
    }

    override fun getColorLevel(): Int {
        Log.d(TAG, "getColorLevel()")
        return PokemonStatTypes.SpecialDefense.ordinal
    }
}