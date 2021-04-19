package com.example.pokedex.data.models

import java.io.Serializable

data class PokemonStatNameAndUrl(
    val name: String,
    val url: String
) : Serializable