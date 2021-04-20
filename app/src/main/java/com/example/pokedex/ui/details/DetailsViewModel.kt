package com.example.pokedex.ui.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.R
import com.example.pokedex.data.events.BaseEvent
import com.example.pokedex.data.managers.PokemonStatsManager
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.stats.PokemonStatPair
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.scope.ScopeApi
import com.example.pokedex.domain.repository.IRepositoryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.pokedex.data.extensions.toLinkedList
import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.domain.persistence.IPersistenceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val eventApi: IEventApi,
    private val repositoryApi: IRepositoryApi,
    private val persistenceApi: IPersistenceApi
) : ViewModel()
{
    companion object {
        private val TAG = DetailsViewModel::class.java.simpleName
    }

    val pokemonDetails : MutableLiveData<PokemonDetails> by lazy {
        MutableLiveData()
    }
    val stats : MutableLiveData<List<PokemonStatPair>?> by lazy {
        MutableLiveData()
    }
    val sprites : MutableLiveData<List<String>?> by lazy {
        MutableLiveData()
    }

    private val alertMessageGeneric : String by lazy {
        context.getString(R.string.alert_generic_error)
    }
    private val alertMessageNetwork : String by lazy {
        context.getString(R.string.alert_generic_network)
    }

    fun initWithPokemonId(pokemonId: String) = viewModelScope.launch {
        Log.d(TAG, "initWithPokemon() pokemonId: $pokemonId")

        val pokemonDetails = withContext(Dispatchers.IO) {
            persistenceApi.findById(pokemonId)
        } ?: throw NullPointerException("pokemonDetails not stored locally")

        val pokemonStats = getStats(pokemonDetails)
        setStats(pokemonStats)

        val pokemonSprites = getSprites(pokemonDetails)
        setSprites(pokemonSprites)

        setPokemon(pokemonDetails)
    }

    /*
        Note: I'm using the IO coroutine dispatcher because I want it to run smoothly
        in the background and not leave the client hanging for its completion
    */
    fun setAsFavourite() = ScopeApi.io().launch {
        Log.d(TAG, "setAsFavourite() pokemon: $pokemonDetails")
        val currentPokemon = pokemonDetails.value ?: throw NullPointerException("pokemon cannot be null")
        try {
            repositoryApi.getPokemonService().setAsFavourite(currentPokemon.pokemonId, currentPokemon)
        } catch (e: Exception) {
            e.printStackTrace()
            handleExceptions(e)
        }
    }

    private fun getStats(pokemonDetails: PokemonDetails) : List<PokemonStatPair> {
        Log.d(TAG, "getStats() pokemon: $pokemonDetails")
        val pokemonStats = mutableListOf<PokemonStatPair>()
        pokemonDetails.stats.forEach {
            val statType = PokemonStatsManager.getStatTypeByRawValue(it.statNameAndUrl.target.name)
            val statDetails = PokemonStatsManager.getStatDetailsByType(statType)
            val statDetailsPair = PokemonStatPair(statDetails, it.value)
            Log.d(TAG, "initWithPokemon() statType: $statType | statDetails: $statDetails | statDetailsPair: $statDetailsPair")
            pokemonStats.add(statDetailsPair)
        }
        return pokemonStats
    }

    private fun getSprites(pokemonDetails: PokemonDetails) : List<String> {
        Log.d(TAG, "getSprites() pokemonDetails: $pokemonDetails")
        return pokemonDetails.sprites.target.toLinkedList()
    }

    private fun handleExceptions(e: Exception) {
        Log.e(TAG, "handleExceptions() cause: ${e.cause} | message: ${e.message}")
        when (e) {
            is SocketTimeoutException, is HttpException -> {
                sendEventErrorNetwork()
            }
            else -> {
                sendEventErrorGeneric()
            }
        }
    }

    private fun setPokemon(value: PokemonDetails?) = viewModelScope.launch {
        Log.d(TAG, "setPokemon() value: $value")
        pokemonDetails.value = value
    }

    private fun setStats(value : List<PokemonStatPair>?) = viewModelScope.launch {
        Log.d(TAG, "setStats() value: $value")
        stats.value = value
    }

    private fun setSprites(value : List<String>?) = viewModelScope.launch {
        Log.d(TAG, "setSprites() value: $value")
        sprites.value = value
    }

    private fun sendEventErrorGeneric() {
        Log.d(TAG, "sendEventErrorGeneric()")
        val event = BaseEvent(
            EventTypes.ErrorGeneric,
            mapOf(EventTypesMapper.MESSAGE to alertMessageGeneric)
        )
        eventApi.publish(event)
    }

    private fun sendEventErrorNetwork() {
        Log.d(TAG, "sendEventErrorNetwork()")
        val event = BaseEvent(
            EventTypes.ErrorNetwork,
            mapOf(EventTypesMapper.MESSAGE to alertMessageNetwork)
        )
        eventApi.publish(event)
    }
}