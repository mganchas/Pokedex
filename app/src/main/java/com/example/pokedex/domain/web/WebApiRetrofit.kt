package com.example.pokedex.domain.web

import android.content.Context
import android.util.Log
import com.example.pokedex.R
import com.example.pokedex.data.repository.IPokemonService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WebApiRetrofit @Inject constructor(
    @ApplicationContext private val appContext: Context
) : IWebApi
{
    companion object {
        private val TAG = WebApiRetrofit::class.java.simpleName
    }

    private val instance by lazy {
        Retrofit.Builder()
            .baseUrl(appContext.resources.getString(R.string.api_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var pokemonService: IPokemonService? = null

    override fun getPokemonService(): IPokemonService {
        Log.d(TAG, "getPokemonService()")
        if (pokemonService == null) {
            pokemonService = instance.create(IPokemonService::class.java)
        }
        return pokemonService!!
    }
}