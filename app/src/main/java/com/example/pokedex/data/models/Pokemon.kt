package com.example.pokedex.data.models

import java.io.Serializable

data class Pokemon(
    var id: String,
    var name : String,
    var url: String,
    var sprites: PokemonSprites?,
    var stats: List<PokemonStat>?
) : Serializable
