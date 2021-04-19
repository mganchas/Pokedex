package com.example.pokedex.ui.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.managers.PokemonStatsManager
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.stats.PokemonStatPair
import kotlinx.coroutines.launch
import toLinkedList

class DetailsViewModel : ViewModel()
{
    companion object {
        private val TAG = DetailsViewModel::class.java.simpleName
    }

    val pokemon : MutableLiveData<Pokemon> by lazy {
        MutableLiveData()
    }
    val stats : MutableLiveData<List<PokemonStatPair>?> by lazy {
        MutableLiveData()
    }
    val sprites : MutableLiveData<List<String>?> by lazy {
        MutableLiveData()
    }

    fun initWithPokemon(pokemon: Pokemon) {
        Log.d(TAG, "initWithPokemon() pokemon: $pokemon")

        val pokemonStats = getStats(pokemon)
        setStats(pokemonStats)

        val pokemonSprites = getSprites(pokemon)
        setSprites(pokemonSprites)

        setPokemon(pokemon)
    }

    private fun getStats(pokemon: Pokemon) : List<PokemonStatPair> {
        Log.d(TAG, "getStats() pokemon: $pokemon")
        val pokemonRawStats = pokemon.stats ?: throw NullPointerException("null stats are not allowed")
        val pokemonStats = mutableListOf<PokemonStatPair>()
        pokemonRawStats.forEach {
            val statType = PokemonStatsManager.getStatTypeByRawValue(it.statNameAndUrl.name)
            val statDetails = PokemonStatsManager.getStatDetailsByType(statType)
            val statDetailsPair = PokemonStatPair(statDetails, it.value)
            Log.d(TAG, "initWithPokemon() statType: $statType | statDetails: $statDetails | statDetailsPair: $statDetailsPair")
            pokemonStats.add(statDetailsPair)
        }
        return pokemonStats
    }

    private fun getSprites(pokemon: Pokemon) : List<String> {
        Log.d(TAG, "getSprites() pokemon: $pokemon")
        val sprites = pokemon.sprites ?: throw NullPointerException("null sprites are not allowed")
        return sprites.toLinkedList()
    }

    private fun setPokemon(value: Pokemon?) = viewModelScope.launch {
        Log.d(TAG, "setPokemon() value: $value")
        pokemon.value = value
    }

    private fun setStats(value : List<PokemonStatPair>?) = viewModelScope.launch {
        Log.d(TAG, "setStats() value: $value")
        stats.value = value
    }

    private fun setSprites(value : List<String>?) = viewModelScope.launch {
        Log.d(TAG, "setSprites() value: $value")
        sprites.value = value
    }
}