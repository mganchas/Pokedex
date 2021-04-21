package com.example.pokedex.data.managers

import android.util.Log
import com.example.pokedex.data.model.pokemon.stats.*
import com.example.pokedex.data.model.pokemon.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStatTypes

object PokemonStatsManager
{
    private val TAG = PokemonStatsManager::class.java.simpleName

    private val statsDetailsMap : Map<PokemonStatTypes, IPokemonStatDetails> by lazy {
        mapOf(
            PokemonStatTypes.HP to PokemonStatDetailsHP(),
            PokemonStatTypes.Attack to PokemonStatDetailsAttack(),
            PokemonStatTypes.SpecialAttack to PokemonStatDetailsSpecialAttack(),
            PokemonStatTypes.Defense to PokemonStatDetailsDefense(),
            PokemonStatTypes.SpecialDefense to PokemonStatDetailsSpecialDefense(),
            PokemonStatTypes.Speed to PokemonStatDetailsSpeed()
        )
    }

    private val rawStatsMap : Map<String, PokemonStatTypes> by lazy {
        mapOf(
            "hp" to PokemonStatTypes.HP,
            "attack" to PokemonStatTypes.Attack,
            "special-attack" to PokemonStatTypes.SpecialAttack,
            "defense" to PokemonStatTypes.Defense,
            "special-defense" to PokemonStatTypes.SpecialDefense,
            "speed" to PokemonStatTypes.Speed
        )
    }

    fun getStatDetailsByType(statType : PokemonStatTypes) : IPokemonStatDetails {
        Log.d(TAG, "getStatDetailsByType() statType: $statType")
        return statsDetailsMap[statType] ?: throw IndexOutOfBoundsException("invalid stat: ${statType.name}")
    }

    fun getStatTypeByRawValue(rawValue : String) : PokemonStatTypes {
        Log.d(TAG, "getStatTypeByRawValue() rawValue: $rawValue")
        if (rawValue.isEmpty()) {
            throw IllegalArgumentException("rawValue cannot be empty")
        }
        return rawStatsMap[rawValue] ?: throw IndexOutOfBoundsException("invalid stat: $rawValue")
    }
}