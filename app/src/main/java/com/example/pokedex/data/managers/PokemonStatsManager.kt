package com.example.pokedex.data.managers

import android.util.Log
import com.example.pokedex.data.models.stats.PokemonStatDetailsAttack
import com.example.pokedex.data.models.stats.PokemonStatDetailsHP
import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStats

object PokemonStatsManager {
    private val statsMap : Map<PokemonStats, IPokemonStatDetails> by lazy {
        mapOf(
            PokemonStats.HP to PokemonStatDetailsHP(),
            PokemonStats.Attack to PokemonStatDetailsAttack(),
            PokemonStats.SpecialAttack to PokemonStatDetailsHP(),
            PokemonStats.Defense to PokemonStatDetailsHP(),
            PokemonStats.SpecialDefense to PokemonStatDetailsHP(),
            PokemonStats.Speed to PokemonStatDetailsHP()
        )
    }

    private val TAG = PokemonStatsManager::class.java.simpleName

    fun getStatDetailsByType(statType : PokemonStats) : IPokemonStatDetails {
        Log.d(TAG, "getStatDetailsByType() statType: $statType")
        return statsMap[statType] ?: throw IndexOutOfBoundsException("invalid stat: ${statType.name}")
    }
}