package com.example.pokedex.data.models.stats

import com.example.pokedex.data.models.stats.abstractions.IPokemonStatDetails

data class PokemonStatPair(
    val details: IPokemonStatDetails,
    val value : Int
)
