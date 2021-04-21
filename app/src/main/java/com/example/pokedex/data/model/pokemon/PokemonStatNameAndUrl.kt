package com.example.pokedex.data.model.pokemon

import java.io.Serializable

data class PokemonStatNameAndUrl(
    val name: String,
    val url: String
) : Serializable