package com.example.pokedex.data.model.pokemon.stats

import com.example.pokedex.data.model.pokemon.stats.abstractions.IPokemonStatDetails

data class PokemonStatPair(
    val details: IPokemonStatDetails,
    val value : Int
)
