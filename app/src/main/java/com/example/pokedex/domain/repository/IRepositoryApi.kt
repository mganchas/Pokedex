package com.example.pokedex.domain.repository

import com.example.pokedex.data.repository.IPokemonService

interface IRepositoryApi {
    fun getPokemonService() : IPokemonService
}