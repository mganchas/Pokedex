package com.example.pokedex.data.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

@Entity
data class PokemonStatNameAndUrl(
    @Id var id: Long = 0,
    var name: String,
    var url: String
) : Serializable