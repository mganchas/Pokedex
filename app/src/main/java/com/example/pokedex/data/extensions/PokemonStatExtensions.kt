package com.example.pokedex.data.extensions

import com.example.pokedex.data.models.PokemonDetailsStats
import com.example.pokedex.data.models.PokemonStat

fun PokemonStat.toPokemonDetailsStats() : PokemonDetailsStats {
    val pokemonDetailsStats = PokemonDetailsStats(value = this.value)
    pokemonDetailsStats.statNameAndUrl.target = this.statNameAndUrl
    return pokemonDetailsStats
}