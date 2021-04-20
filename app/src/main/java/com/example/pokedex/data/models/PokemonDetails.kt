package com.example.pokedex.data.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.io.Serializable

@Entity
data class PokemonDetails(
    @Id var id: Long = 0,
    var pokemonId: String,
    var name : String,
    var url: String?,
) : Serializable {
    lateinit var sprites: ToOne<PokemonSprites>
    lateinit var stats: ToMany<PokemonDetailsStats>
}
