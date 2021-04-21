package com.example.pokedex.data.repository

import com.example.pokedex.data.model.pokemon.Pokemon
import com.example.pokedex.data.model.pokemon.PokemonSearch
import retrofit2.http.*

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

    /*
        Note: At first, this url was being passed as a parameter from its invoker (it was stored
        as a resource string, just like the base api url), but I changed that because it makes
        more sense (to me, at least) that the invoker (a viewModel, an activity, fragment, ...)
        should not need to know the endpoints accessed and therefore it should be a concern only
        for the api invoker (in this case IPokemonService)
    */
    @POST("https://webhook.site./c198ff0a-488a-4238-999e-36673d6e9654/{id}")
    suspend fun setAsFavourite(@Path("id") id : String, @Body pokemon: Pokemon)
}