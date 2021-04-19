package com.example.pokedex.data.managers

import android.util.Log
import com.example.pokedex.data.models.stats.*
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStats

object PokemonStatsManager
{
    private val TAG = PokemonStatsManager::class.java.simpleName

    private val statsMap : Map<PokemonStats, IPokemonStatDetails> by lazy {
        mapOf(
            PokemonStats.HP to PokemonStatDetailsHP(),
            PokemonStats.Attack to PokemonStatDetailsAttack(),
            PokemonStats.SpecialAttack to PokemonStatDetailsSpecialAttack(),
            PokemonStats.Defense to PokemonStatDetailsDefense(),
            PokemonStats.SpecialDefense to PokemonStatDetailsSpecialDefense(),
            PokemonStats.Speed to PokemonStatDetailsSpeed()
        )
    }

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

    fun getStatDetailsByType(statType : PokemonStats) : IPokemonStatDetails {
        Log.d(TAG, "getStatDetailsByType() statType: $statType")
        return statsMap[statType] ?: throw IndexOutOfBoundsException("invalid stat: ${statType.name}")
    }

    fun getStatTypeByRawValue(rawValue : String) : PokemonStats {
        Log.d(TAG, "getStatTypeByRawValue() rawValue: $rawValue")
        return rawStatsMap[rawValue] ?: throw IndexOutOfBoundsException("invalid stat: $rawValue")
    }
}