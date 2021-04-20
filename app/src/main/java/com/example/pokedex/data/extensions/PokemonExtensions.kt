package com.example.pokedex.data.extensions

import android.util.Log
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonDetails

fun Pokemon.toPokemonDetails() : PokemonDetails {
    Log.d("PokemonExtensions", "toPokemonDetails() pokemon: $this")

    val pokemonDetails = PokemonDetails(
        pokemonId = this.id,
        name = this.name,
        url = this.url,
    ).also {
        it.sprites.target = this.sprites
    }
    this.stats?.forEach {
        pokemonDetails.stats.add(it.toPokemonDetailsStats())
    }
    return pokemonDetails
}