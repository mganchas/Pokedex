package com.example.pokedex.domain.persistence

import com.example.pokedex.data.models.PokemonDetails

interface IPersistenceApi {
    suspend fun getAll(): List<PokemonDetails>
    suspend fun findByName(name: String): PokemonDetails?
    suspend fun findById(id: String): PokemonDetails?
    suspend fun add(pokemon: PokemonDetails)
    suspend fun addMany(pokemons: List<PokemonDetails>)
    suspend fun remove(pokemon: PokemonDetails)
    suspend fun removeAll()
}