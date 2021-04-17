package com.example.pokedex.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.R
import com.example.pokedex.data.events.BaseEvent
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonSearch
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.parsers.IUrlParserApi
import com.example.pokedex.domain.web.IWebApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val eventApi: IEventApi,
    private val webApi: IWebApi,
    private val urlParserApi: IUrlParserApi
) : ViewModel()
{
    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    val pokemonSearch: MutableLiveData<PokemonSearch> by lazy {
        MutableLiveData<PokemonSearch>(null)
    }
    val isPreviousNavigationButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isNextNavigationButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val pokemonsCount : MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }

    private var searchValue: String? = null
    private var pageLimit = context.resources.getInteger(R.integer.api_default_limit)

    private val errorMessageInputInvalid : String by lazy {
        context.getString(R.string.alert_input_invalid)
    }
    private val errorMessageGeneric : String by lazy {
        context.getString(R.string.alert_generic_error)
    }
    private val errorMessageNetwork : String by lazy {
        context.getString(R.string.alert_generic_network)
    }

    fun onPrevious() = viewModelScope.launch {
        Log.d(TAG, "onPrevious()")
        val searchUrl = pokemonSearch.value?.previous ?: ""
        Log.d(TAG, "onPrevious() searchUrl: $searchUrl")

        if (searchUrl.isEmpty()) {
            throw NullPointerException("no previous url to navigate")
        }

        // TODO: igual ao search
    }

    fun onNext() = viewModelScope.launch {
        Log.d(TAG, "onNext()")
        val searchUrl = pokemonSearch.value?.next ?: ""
        Log.d(TAG, "onNext() searchUrl: $searchUrl")

        if (searchUrl.isEmpty()) {
            throw NullPointerException("no next url to navigate")
        }

        // TODO: igual ao search
    }

    fun onPokemonDetail() {
        Log.d(TAG, "onPokemonDetail()")
        // TODO: ir à lista e passar para o controller o value de 'Pokemon' ...

    }

    fun onSearch() {
        Log.d(TAG, "onSearch() searchValue: $searchValue")
        val value = searchValue ?: ""
        when (value.isEmpty()) {
            true -> onEmptySearch()
            false -> onValueSearch(value)
        }
    }

    fun onNewLimit(newLimit : Int) {
        Log.d(TAG, "onNewLimit() newLimit: $newLimit")
        pageLimit = newLimit
        if (pokemonSearch.value != null) {
            onSearch()
        }
    }

    fun setSearchValue(newValue : String) {
        Log.d(TAG, "setSearchValue() newValue: $newValue")
        searchValue = newValue
    }

    private fun onValueSearch(value: String) {
        Log.d(TAG, "onValueSearch() value: $value")
        // TODO: validar localmente (persistencia do dispositivo) se não tem já estes dados
    }

    private suspend fun getPokemonByValue(value: String) {
        Log.d(TAG, "getPokemonByValue() value: $value")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonDetailsByValue(value)
        }
    }

    private fun onEmptySearch() {
        Log.d(TAG, "onEmptySearch()")
        sendEventShowLoading()
        viewModelScope.launch {
            try {
                clearPokemonList()
                val searchResults = getPokemons()
                Log.d(TAG, "onEmptySearch() searchResults: $searchResults")

                searchResults.results.forEach {
                    it.id = urlParserApi.getLastPath(it.url)
                    val pokemonDetails = getPokemonDetailsByUrl(it.url)
                    Log.d(TAG, "pokemonDetails: $pokemonDetails")
                    it.sprites = pokemonDetails.sprites
                }

                setIsPreviousNavigationButtonsEnabled(searchResults.previous != null)
                setIsNextNavigationButtonsEnabled(searchResults.next != null)
                setPokemonsCount(searchResults.count)

                pokemonSearch.value = searchResults
                sendEventHideLoading()
            } catch (e: Exception) {
                sendEventHideLoading()
                handleExceptions(e)
            }
        }
    }

    private suspend fun getPokemons() : PokemonSearch {
        Log.d(TAG, "getPokemons()")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonsWithLimit(pageLimit)
        }
    }

    private suspend fun getPokemonsByUrl(url: String) : PokemonSearch {
        Log.d(TAG, "getPokemonsByUrl()")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonsByUrl(url)
        }
    }

    private suspend fun getPokemonDetailsByUrl(url: String) : Pokemon {
        Log.d(TAG, "getPokemonByUrl()")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonDetailsByUrl(url)
        }
    }

    private fun clearPokemonList() = viewModelScope.launch {
        Log.d(TAG, "clearPokemonList()")
        pokemonSearch.value = null
    }

    private fun handleExceptions(e: Exception) {
        Log.e(TAG, "handleExceptions() cause: ${e.cause} | message: ${e.message}")
        when (e) {
            is SocketTimeoutException -> {
                sendEventErrorNetwork()
            }
            else -> {
                sendEventErrorGeneric()
            }
        }
    }

    private fun setIsPreviousNavigationButtonsEnabled(value: Boolean) = viewModelScope.launch {
        Log.d(TAG, "setIsPreviousNavigationButtonsEnabled() value: $value")
        isPreviousNavigationButtonEnabled.value = value
    }

    private fun setIsNextNavigationButtonsEnabled(value: Boolean) = viewModelScope.launch {
        Log.d(TAG, "setIsNextNavigationButtonsEnabled() value: $value")
        isNextNavigationButtonEnabled.value = value
    }

    private fun setPokemonsCount(value: Int) = viewModelScope.launch {
        Log.d(TAG, "setPokemonsCount() value: $value")
        pokemonsCount.value = value
    }

    /*
        Note: If there were requirements stating that an empty input value is not valid, we'd send
            and event for this specific error type.
            I've made the decision that an empty input value is used to retrieve all pokemons.
     */
    private fun sendEventErrorInvalidInput() {
        Log.d(TAG, "sendEventErrorInvalidInput()")
        val event = BaseEvent(
            EventTypes.SearchErrorInvalidInput,
            mapOf(EventTypesMapper.MESSAGE to errorMessageInputInvalid)
        )
        eventApi.publish(event)
    }

    private fun sendEventErrorGeneric() {
        Log.d(TAG, "sendEventErrorGeneric()")
        val event = BaseEvent(
            EventTypes.SearchErrorGeneric,
            mapOf(EventTypesMapper.MESSAGE to errorMessageGeneric)
        )
        eventApi.publish(event)
    }

    private fun sendEventErrorNetwork() {
        Log.d(TAG, "sendEventErrorNetwork()")
        val event = BaseEvent(
            EventTypes.SearchErrorNetwork,
            mapOf(EventTypesMapper.MESSAGE to errorMessageNetwork)
        )
        eventApi.publish(event)
    }

    private fun sendEventShowLoading() {
        Log.d(TAG, "sendEventShowLoading()")
        val event = BaseEvent(EventTypes.ShowLoading)
        eventApi.publish(event)
    }

    private fun sendEventHideLoading() {
        Log.d(TAG, "sendEventHideLoading()")
        val event = BaseEvent(EventTypes.HideLoading)
        eventApi.publish(event)
    }
}