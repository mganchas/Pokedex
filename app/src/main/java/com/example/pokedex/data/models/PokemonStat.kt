package com.example.pokedex.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PokemonStat(
    @SerializedName("base_stat") val value: Int,
    @SerializedName("stat") val statNameAndUrl: PokemonStatNameAndUrl
) : Serializable
