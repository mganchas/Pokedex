package com.example.pokedex.data.mappers

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

    fun getStatTypeByRawValue(rawValue : String) : PokemonStats {
        return rawStatsMap[rawValue] ?:
            throw IndexOutOfBoundsException("invalid stat: $rawValue")
    }
}