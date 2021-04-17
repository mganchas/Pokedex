package com.example.pokedex.data.models

data class Pokemon(
    val name : String,
    val url: String,
    var id: String?,
    var sprites: PokemonSprites?
)
