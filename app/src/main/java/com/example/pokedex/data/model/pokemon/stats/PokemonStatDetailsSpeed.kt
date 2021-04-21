package com.example.pokedex.data.model.pokemon.stats

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pokedex.R
import com.example.pokedex.data.model.pokemon.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStatTypes

class PokemonStatDetailsSpeed : IPokemonStatDetails {
    companion object {
        private val TAG = PokemonStatDetailsSpeed::class.java.simpleName
    }

    override fun getName(context: Context) : String {
        Log.d(TAG, "getName()")
        return context.getString(R.string.stat_speed)
    }

    override fun getIcon(context: Context): Drawable? {
        Log.d(TAG, "getIcon()")
        return ContextCompat.getDrawable(context, R.drawable.ic_spark)
    }

    override fun getColorLevel(): Int {
        Log.d(TAG, "getColorLevel()")
        return PokemonStatTypes.Speed.ordinal
    }
}