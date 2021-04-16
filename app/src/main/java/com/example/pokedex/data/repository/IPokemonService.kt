package com.example.pokedex.data.repository

import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.data.models.PokemonSearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface IPokemonService {
    @GET("pokemon")
    suspend fun getPokemons() : PokemonSearch

    @GET
    suspend fun getPokemonsByUrl(@Url url : String) : PokemonSearch

    @GET("pokemon/{value}")
    suspend fun getPokemonDetailsByValue(@Path("value") value : String) : PokemonDetails

    @GET
    suspend fun getPokemonDetailsByUrl(@Url url : String) : PokemonDetails
}