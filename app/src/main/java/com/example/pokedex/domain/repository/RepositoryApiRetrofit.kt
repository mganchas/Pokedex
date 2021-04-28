package com.example.pokedex.domain.repository

import android.content.Context
import android.util.Log
import com.example.pokedex.R
import com.example.pokedex.data.model.pokemon.Pokemon
import com.example.pokedex.data.model.pokemon.PokemonSearch
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RepositoryApiRetrofit @Inject constructor(
    @ApplicationContext private val appContext: Context
) : IRepositoryApi
{
    companion object {
        private val TAG = RepositoryApiRetrofit::class.java.simpleName
    }

    private val instance by lazy {
        Retrofit.Builder()
            .baseUrl(appContext.getString(R.string.api_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var pokemonService: IPokemonService? = null

    private fun getPokemonService(): IPokemonService {
        Log.d(TAG, "getPokemonService()")
        if (pokemonService == null) {
            pokemonService = instance.create(IPokemonService::class.java)
        }
        return pokemonService!!
    }

    override suspend fun getPokemons(): PokemonSearch = getPokemonService().getPokemons()

    override suspend fun getPokemonsWithLimit(limit: Int): PokemonSearch = getPokemonService().getPokemonsWithLimit(limit)

    override suspend fun getPokemonsByUrl(url: String): PokemonSearch = getPokemonService().getPokemonsByUrl(url)

    override suspend fun getPokemonByValue(value: String): Pokemon = getPokemonService().getPokemonByValue(value)

    override suspend fun getPokemonByUrl(url: String): Pokemon = getPokemonService().getPokemonByUrl(url)

    override suspend fun setAsFavourite(id: String, pokemon: Pokemon) = getPokemonService().setAsFavourite(id, pokemon)
}