package com.example.pokedex.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.R
import com.example.pokedex.data.events.BaseEvent
import com.example.pokedex.data.extensions.toPokemonDetails
import com.example.pokedex.data.extensions.toPokemonDetailsStats
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.data.models.PokemonSearch
import com.example.pokedex.data.persistence.ObjectBox
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.parsers.IUrlParserApi
import com.example.pokedex.domain.persistence.IPersistenceApi
import com.example.pokedex.domain.repository.IRepositoryApi
import com.example.pokedex.domain.scope.ScopeApi
import com.example.pokedex.ui.details.DetailsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val eventApi: IEventApi,
    private val repositoryApi: IRepositoryApi,
    private val urlParserApi: IUrlParserApi,
    private val persistenceApi: IPersistenceApi
) : ViewModel()
{
    companion object {
        private val TAG = MainViewModel::class.java.simpleName

        const val KEY_SEARCH_TEXT = "searchText"
        const val KEY_POKEMON_SEARCH = "pokemonSearch"
        const val KEY_POKEMON_DETAILS_LIST = "pokemonDetailsList"
    }

    val pokemonDetailsList: MutableLiveData<List<PokemonDetails>?> by lazy {
        MutableLiveData()
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

    private var pokemonSearch: PokemonSearch? = null
    private var searchText: String? = null
    private var pageLimit = context.resources.getInteger(R.integer.api_default_limit)

    private val alertMessageInputInvalid : String by lazy {
        context.getString(R.string.alert_input_invalid)
    }
    private val alertMessageNotFound : String by lazy {
        context.getString(R.string.alert_not_found)
    }
    private val alertMessageGeneric : String by lazy {
        context.getString(R.string.alert_generic_error)
    }
    private val alertMessageNetwork : String by lazy {
        context.getString(R.string.alert_generic_network)
    }

    fun onSaveInstanceState(bundle: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        savePokemonDetailsListState(bundle)
        savePokemonSearchState(bundle)
        saveSearchTextState(bundle)
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        getPokemonDetailsListState(bundle)
        getPokemonSearchState(bundle)
        getSearchTextState(bundle)
    }

    fun onPrevious() {
        Log.d(TAG, "onPrevious()")
        val searchUrl = pokemonSearch?.previous ?: ""
        Log.d(TAG, "onPrevious() searchUrl: $searchUrl")
        if (searchUrl.isEmpty()) {
            throw NullPointerException("no previous url to navigate")
        }
        onCollectionSearch(searchUrl)
    }

    fun onNext() {
        Log.d(TAG, "onNext()")
        val searchUrl = pokemonSearch?.next ?: ""
        Log.d(TAG, "onNext() searchUrl: $searchUrl")
        if (searchUrl.isEmpty()) {
            throw NullPointerException("no next url to navigate")
        }
        onCollectionSearch(searchUrl)
    }

    fun onPokemonDetail(pokemonDetails: PokemonDetails) {
        Log.d(TAG, "onPokemonDetail() pokemonDetails: $pokemonDetails")
        sendEventNavigateToDetails(pokemonDetails.pokemonId)
    }

    fun onSearch() {
        Log.d(TAG, "onSearch() searchValue: $searchText")
        val value = searchText ?: ""
        when (value.isEmpty()) {
            true -> onCollectionSearch(null)
            false -> onSingleSearch(value)
        }
    }

    fun onNewLimit(newLimit : Int) {
        Log.d(TAG, "onNewLimit() newLimit: $newLimit")
        pageLimit = newLimit

        // to update search only if there has been a prior search
        if (pokemonSearch != null) {
            onSearch()
        }
    }

    fun setSearchText(newValue : String) {
        Log.d(TAG, "setSearchText() newValue: $newValue")
        searchText = newValue.trim().toLowerCase(Locale.ROOT)
    }

    private fun savePokemonDetailsListState(bundle: Bundle) {
        Log.d(TAG, "savePokemonDetailsListState()")
        bundle.putSerializable(KEY_POKEMON_DETAILS_LIST, pokemonSearch)
    }

    private fun savePokemonSearchState(bundle: Bundle) {
        Log.d(TAG, "savePokemonSearchState()")
        bundle.putSerializable(KEY_POKEMON_SEARCH, pokemonSearch)
    }

    private fun saveSearchTextState(bundle: Bundle) {
        Log.d(TAG, "saveSearchTextState()")
        bundle.putString(KEY_SEARCH_TEXT, searchText)
    }

    private fun getPokemonDetailsListState(bundle: Bundle) {
        Log.d(TAG, "getPokemonDetailsListState()")
        bundle.getSerializable(KEY_POKEMON_DETAILS_LIST)?.let {
            setPokemonDetailsList(it as List<PokemonDetails>?)
        }
    }

    private fun getPokemonSearchState(bundle: Bundle) {
        Log.d(TAG, "getPokemonSearchState()")
        bundle.getSerializable(KEY_POKEMON_SEARCH)?.let {
            setPokemonSearch(it as PokemonSearch?)
        }
    }

    private fun getSearchTextState(bundle: Bundle) {
        Log.d(TAG, "getSearchTextState()")
        bundle.getString(KEY_SEARCH_TEXT)?.let {
            setSearchText(it)
        }
    }

    private fun onSingleSearch(value: String) = viewModelScope.launch {
        Log.d(TAG, "onValueSearch() value: $value")

        sendEventShowLoading()
        try {
            clearResults()
            val pokemon = getPokemonByValue(value)
            Log.d(TAG, "onValueSearch() pokemon: $pokemon")
            val searchResults = PokemonSearch(
                count = 1,
                next = null,
                previous = null,
                results = listOf(pokemon)
            )
            setPokemonsCount(searchResults.count)
            setPokemonSearch(searchResults)

            val pokemonDetailsList = mutableListOf<PokemonDetails>()
            val pokemonDetails = pokemon.toPokemonDetails()
            pokemonDetailsList.add(pokemonDetails)
            addToLocalDatabase(pokemonDetails)
            setPokemonDetailsList(pokemonDetailsList)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
            //handleExceptions(e)
        }
        finally {
            sendEventHideLoading()
        }
    }

    private suspend fun getPokemonByValue(value: String) : Pokemon {
        Log.d(TAG, "getPokemonByValue() value: $value")
        return withContext(Dispatchers.IO) {
            repositoryApi.getPokemonService().getPokemonByValue(value)
        }
    }

    private fun clearResults() = viewModelScope.launch {
        Log.d(TAG, "clearResults()")
        setPokemonDetailsList(null)
        setPokemonsCount(0)
        setPokemonSearch(null)
        setIsPreviousNavigationButtonsEnabled(false)
        setIsNextNavigationButtonsEnabled(false)
    }

    private fun onCollectionSearch(searchUrl: String?) = viewModelScope.launch {
        Log.d(TAG, "doCollectionSearch()")
        sendEventShowLoading()
        try {
            clearResults()
            val searchResults = if (searchUrl != null) getPokemonsByUrl(searchUrl) else getPokemons()
            Log.d(TAG, "doCollectionSearch() searchResults: $searchResults")
            setIsPreviousNavigationButtonsEnabled(searchResults.previous != null)
            setIsNextNavigationButtonsEnabled(searchResults.next != null)
            setPokemonsCount(searchResults.count)
            setPokemonSearch(searchResults)

            val pokemonDetailsList = mutableListOf<PokemonDetails>()
            searchResults.results.forEach {
                val pokemonDetails = getPokemonDetailsForResults(it)
                pokemonDetailsList.add(pokemonDetails)
                addToLocalDatabase(pokemonDetails)
            }
            setPokemonDetailsList(pokemonDetailsList)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
            //handleExceptions(e)
        }
        finally {
            sendEventHideLoading()
        }
    }

    private suspend fun getPokemons() : PokemonSearch {
        Log.d(TAG, "getPokemons()")
        return withContext(Dispatchers.IO) {
            repositoryApi.getPokemonService().getPokemonsWithLimit(pageLimit)
        }
    }

    private suspend fun getPokemonsByUrl(url: String) : PokemonSearch {
        Log.d(TAG, "getPokemonsByUrl()")
        return withContext(Dispatchers.IO) {
            repositoryApi.getPokemonService().getPokemonsByUrl(url)
        }
    }

    private suspend fun getPokemonDetailsForResults(pokemon: Pokemon) : PokemonDetails {
        Log.d(TAG, "getPokemonDetailsForResults()")
        return when (val persistedPokemonDetails = getPokemonDetailsByName(pokemon.name)) {
            null -> {
                getPokemonByUrl(pokemon.url).also {
                    it.url = pokemon.url
                }.toPokemonDetails()
            }
            else -> persistedPokemonDetails
        }
    }

    private suspend fun getPokemonDetailsByName(name: String) : PokemonDetails? {
        Log.d(TAG, "getPokemonDetailsByName() name: $name")
        return withContext(Dispatchers.IO) {
            persistenceApi.findByName(name)
        }
    }

    private suspend fun getPokemonByUrl(url: String) : Pokemon {
        Log.d(TAG, "getPokemonByUrl() url: $url")
        return withContext(Dispatchers.IO) {
            repositoryApi.getPokemonService().getPokemonByUrl(url)
        }
    }

    private fun addToLocalDatabase(pokemonDetails: PokemonDetails) = ScopeApi.io().launch {
        Log.d(TAG, "addToLocalDatabase() pokemonDetails: $pokemonDetails")
        persistenceApi.add(pokemonDetails)
    }

    private fun handleExceptions(e: Exception) {
        Log.e(TAG, "handleExceptions() cause: ${e.cause} | message: ${e.message}")
        when (e) {
            is SocketTimeoutException -> {
                sendEventErrorNetwork()
            }
            is HttpException -> {
                if (e.code() == 404) {
                    sendEventNotFound()
                }
                else {
                    sendEventErrorNetwork()
                }
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

    private fun setPokemonsCount(value: Int)  {
        Log.d(TAG, "setPokemonsCount() value: $value")
        pokemonsCount.value = value
    }

    private fun setPokemonDetailsList(value: List<PokemonDetails>?)  {
        Log.d(TAG, "setPokemonDetailsList() value: $value")
        pokemonDetailsList.value = value
    }

    private fun setPokemonSearch(value: PokemonSearch?) {
        Log.d(TAG, "setPokemonSearch() value: $value")
        pokemonSearch = value
    }

    /*
        Note: If there were requirements stating that an empty input value is not valid, we'd send
            and event for this specific error type.
            I've made the decision that an empty input value is used to retrieve all pokemons.
     */
    private fun sendEventErrorInvalidInput() {
        Log.d(TAG, "sendEventErrorInvalidInput()")
        val event = BaseEvent(
            EventTypes.InvalidInput,
            mapOf(EventTypesMapper.MESSAGE to alertMessageInputInvalid)
        )
        eventApi.publish(event)
    }

    private fun sendEventNotFound() {
        Log.d(TAG, "sendEventNotFound()")
        val event = BaseEvent(
            EventTypes.PokemonNotFound,
            mapOf(EventTypesMapper.MESSAGE to alertMessageNotFound)
        )
        eventApi.publish(event)
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

    private fun sendEventNavigateToDetails(pokemonId: String) {
        Log.d(TAG, "sendEventNavigateToDetails()")
        val event = BaseEvent(
            EventTypes.NavigateTo,
            mapOf(
                EventTypesMapper.NAVIGATION_TO to DetailsActivity::class.java,
                EventTypesMapper.NAVIGATION_DATA to pokemonId
            )
        )
        eventApi.publish(event)
    }
}