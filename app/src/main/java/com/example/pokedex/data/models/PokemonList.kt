package com.example.pokedex.data.models

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<PokemonNameAndUrl>,
)
