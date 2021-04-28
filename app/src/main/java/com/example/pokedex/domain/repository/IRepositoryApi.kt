package com.example.pokedex.domain.repository

import com.example.pokedex.data.model.pokemon.Pokemon
import com.example.pokedex.data.model.pokemon.PokemonSearch
import retrofit2.http.*

interface IRepositoryApi {
    suspend fun getPokemons() : PokemonSearch
    suspend fun getPokemonsWithLimit(@Query("limit") limit : Int) : PokemonSearch
    suspend fun getPokemonsByUrl(@Url url : String) : PokemonSearch
    suspend fun getPokemonByValue(@Path("value") value : String) : Pokemon
    suspend fun getPokemonByUrl(@Url url : String) : Pokemon
    suspend fun setAsFavourite(@Path("id") id : String, @Body pokemon: Pokemon)
}