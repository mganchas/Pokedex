package com.example.pokedex.data.model.pokemon

import java.io.Serializable

data class PokemonSearch(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>,
) : Serializable
