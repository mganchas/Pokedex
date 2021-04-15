package com.example.pokedex.data.mappers

import android.util.Log
import com.example.pokedex.data.types.PokemonStats

object PokemonStatsMapper
{
    private val rawStatsMap : Map<String, PokemonStats> by lazy {
        mapOf(
            "hp" to PokemonStats.HP,
            "attack" to PokemonStats.Attack,
            "special-attack" to PokemonStats.SpecialAttack,
            "defense" to PokemonStats.Defense,
            "special-defense" to PokemonStats.SpecialDefense,
            "speed" to PokemonStats.Speed
        )
    }

    private val TAG = PokemonStatsMapper::class.java.simpleName

    fun getStatTypeByRawValue(rawValue : String) : PokemonStats {
        Log.d(TAG, "getStatTypeByRawValue() rawValue: $rawValue")
        return rawStatsMap[rawValue] ?: throw IndexOutOfBoundsException("invalid stat: $rawValue")
    }
}