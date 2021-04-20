package com.example.pokedex.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PokemonStat(
    @SerializedName("base_stat") var value: Int,
    @SerializedName("stat") var statNameAndUrl: PokemonStatNameAndUrl
) : Serializable
