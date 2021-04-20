package com.example.pokedex.data.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable

@Entity
data class PokemonDetailsStats(
    @Id var id: Long = 0,
    var value: Int,
): Serializable {
    lateinit var statNameAndUrl: ToOne<PokemonStatNameAndUrl>
}
