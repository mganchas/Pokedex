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
import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.data.models.PokemonSearch
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.image.IImageApi
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
    val imageApi: IImageApi,
    val eventApi: IEventApi,
    val webApi: IWebApi,
    val urlParserApi: IUrlParserApi
) : ViewModel()
{
    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    val pokemons: MutableLiveData<MutableList<Pokemon>> by lazy {
        MutableLiveData<MutableList<Pokemon>>()
    }
    val areNavigationButtonsVisible: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isPreviousNavigationButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isNextNavigationButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    private var pokemonSearch : PokemonSearch? = null

    private val errorMessageInputInvalid = context.resources.getString(R.string.alert_input_invalid)
    private val errorMessageGeneric = context.resources.getString(R.string.alert_generic_error)
    private val errorMessageNetwork = context.resources.getString(R.string.alert_generic_network)

    fun onPrevious() = viewModelScope.launch {
        Log.d(TAG, "onPrevious()")
        val searchUrl = pokemonSearch?.previous ?: ""
        Log.d(TAG, "onPrevious() searchUrl: $searchUrl")

        if (searchUrl.isEmpty()) {
            throw NullPointerException("no previous url to navigate")
        }

        sendEventShowLoading()
        try {
            clearPokemonList()
            val searchResults = getPokemonsByUrl(searchUrl)
            Log.d(TAG, "onEmptySearch() searchResults: $searchResults")

            setAreNavigationButtonsVisible(searchResults.results.isNotEmpty())
            setIsPreviousNavigationButtonsEnabled(searchResults.previous != null)
            setIsNextNavigationButtonsEnabled(searchResults.next != null)

            // TODO: implementar ...
            pokemonSearch = searchResults
            sendEventHideLoading()
        } catch (e: Exception) {
            sendEventHideLoading()
            handleExceptions(e)
        }
    }

    fun onNext() = viewModelScope.launch {
        Log.d(TAG, "onNext()")
        val searchUrl = pokemonSearch?.next ?: ""
        Log.d(TAG, "onNext() searchUrl: $searchUrl")

        if (searchUrl.isEmpty()) {
            throw NullPointerException("no next url to navigate")
        }

        sendEventShowLoading()
        try {
            clearPokemonList()
            val searchResults = getPokemonsByUrl(searchUrl)
            Log.d(TAG, "onEmptySearch() searchResults: $searchResults")

            setAreNavigationButtonsVisible(searchResults.results.isNotEmpty())
            setIsPreviousNavigationButtonsEnabled(searchResults.previous != null)
            setIsNextNavigationButtonsEnabled(searchResults.next != null)

            // TODO: implementar ...
            pokemonSearch = searchResults
            sendEventHideLoading()
        } catch (e: Exception) {
            sendEventHideLoading()
            handleExceptions(e)
        }
    }

    fun onPokemonDetail() {
        Log.d(TAG, "onPokemonDetail()")
        // TODO: implementar ...

    }

    fun onSearch(value: String) {
        Log.d(TAG, "onSearch()")
        when (value.isEmpty()) {
            true -> onEmptySearch()
            false -> onValueSearch(value)
        }
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
                }

                setAreNavigationButtonsVisible(searchResults.results.isNotEmpty())
                setIsPreviousNavigationButtonsEnabled(searchResults.previous != null)
                setIsNextNavigationButtonsEnabled(searchResults.next != null)

                pokemonSearch = searchResults
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
            webApi.getPokemonService().getPokemons()
        }
    }

    private suspend fun getPokemonsByUrl(url: String) : PokemonSearch {
        Log.d(TAG, "getPokemonsByUrl()")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonsByUrl(url)
        }
    }

    private suspend fun getPokemonDetailsByUrl(url: String) : PokemonDetails {
        Log.d(TAG, "getPokemonByUrl()")
        return withContext(Dispatchers.IO) {
            webApi.getPokemonService().getPokemonDetailsByUrl(url)
        }
    }

    private fun clearPokemonList() = viewModelScope.launch {
        Log.d(TAG, "clearPokemonList()")
        pokemons.value = mutableListOf()
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

    private fun setAreNavigationButtonsVisible(value: Boolean) = viewModelScope.launch {
        areNavigationButtonsVisible.value = value
    }

    private fun setIsPreviousNavigationButtonsEnabled(value: Boolean) = viewModelScope.launch {
        isPreviousNavigationButtonEnabled.value = value
    }

    private fun setIsNextNavigationButtonsEnabled(value: Boolean) = viewModelScope.launch {
        isNextNavigationButtonEnabled.value = value
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