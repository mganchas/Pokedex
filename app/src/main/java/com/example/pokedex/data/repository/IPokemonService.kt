package com.example.pokedex.data.repository

import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonSearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface IPokemonService {
    @GET("pokemon")
    suspend fun getPokemons() : PokemonSearch

    @GET("pokemon")
    suspend fun getPokemonsWithLimit(@Query("limit") limit : Int) : PokemonSearch

    @GET
    suspend fun getPokemonsByUrl(@Url url : String) : PokemonSearch

    @GET("pokemon/{value}")
    suspend fun getPokemonByValue(@Path("value") value : String) : Pokemon

    @GET
    suspend fun getPokemonByUrl(@Url url : String) : Pokemon
}