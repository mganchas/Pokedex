package com.example.pokedex.domain.web

import com.example.pokedex.data.repository.IPokemonService

interface IWebApi {
    fun getPokemonService() : IPokemonService
}