package com.example.pokedex.data.extensions

import com.example.pokedex.data.model.pokemon.PokemonSprites
import java.util.*

fun PokemonSprites.toLinkedList() : LinkedList<String> {
    val list = LinkedList<String>()
    list.addIfNotNull(this.frontShiny)
    list.addIfNotNull(this.backShiny)

    list.addIfNotNull(this.frontDefault)
    list.addIfNotNull(this.backDefault)

    list.addIfNotNull(this.frontFemale)
    list.addIfNotNull(this.backFemale)

    list.addIfNotNull(this.frontShinyFemale)
    list.addIfNotNull(this.backShinyFemale)
    return list
}