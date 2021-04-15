package com.example.pokedex.data.repository

import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface IPokemonService {
    /*
    @GET("/")
    suspend fun getAllPokemons(
        @Query("pageLimit") pageLimit : Int?,
        @Query("offset") offset : Int?
    ) : Call<List<Pokemon>>
     */

    @GET("pokemon")
    suspend fun getAllPokemons() : PokemonList

    @GET("pokemon/{value}")
    suspend fun getPokemonByValue(@Path("value") value : String) : Pokemon

    @GET
    suspend fun getPokemonByUrl(@Url url : String) : Pokemon
}