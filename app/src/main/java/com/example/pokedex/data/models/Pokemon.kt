package com.example.pokedex.data.models

import java.io.Serializable

data class Pokemon(
    val name : String,
    val url: String,
    var id: String?,
    var sprites: PokemonSprites?,
    var stats: List<PokemonStat>?
) : Serializable
