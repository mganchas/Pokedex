package com.example.pokedex.data.models

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

@Entity
data class PokemonSprites(
    @Id var id: Long = 0,
    @SerializedName("back_default") var backDefault: String?,
    @SerializedName("back_female") var backFemale: String?,
    @SerializedName("back_shiny") var backShiny: String?,
    @SerializedName("back_shiny_female") var backShinyFemale: String?,
    @SerializedName("front_default") var frontDefault: String?,
    @SerializedName("front_female") var frontFemale: String?,
    @SerializedName("front_shiny") var frontShiny: String?,
    @SerializedName("front_shiny_female") var frontShinyFemale: String?,
) : Serializable
