package com.example.pokedex.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.R
import com.example.pokedex.data.events.OnLoadingEvent
import com.example.pokedex.data.events.OnSearchErrorEvent
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.image.IImageApi
import com.example.pokedex.domain.web.IWebApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val imageApi: IImageApi,
    val eventApi: IEventApi,
    val webApi: IWebApi
) : ViewModel()
{
    val pokemons: MutableLiveData<MutableList<Pokemon>> by lazy {
        MutableLiveData<MutableList<Pokemon>>()
    }

    val areNavigationButtonsVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPreviousNavigationButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNextNavigationButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    private val errorMessageInputInvalid = context.resources.getString(R.string.alert_input_invalid)
    private val errorMessageGeneric = context.resources.getString(R.string.alert_generic_error)

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun onPokemonDetail() {
        Log.d(TAG, "onPokemonDetail()")
    }

    fun onSearch(value: String) {
        Log.d(TAG, "onSearch()")
        sendEventShowLoading()

        /*
            Note: I'm leaving this commented to show what would be a possible implementation if
            there were requirements stating that an empty input value is not valid.
            I've made the decision that an empty input value is used to retrieve all pokemons.
            if (value.isEmpty()) {
                sendEventErrorInvalidInput()
                return
            }
        */

        if (value.isEmpty()) {
            viewModelScope.launch {
                getAllPokemons()
            }
            return
        }

        // TODO: ....
    }

    private suspend fun getAllPokemons()
    {
        Log.d(TAG, "getAllPokemons()")
        val pokemonList = withContext(Dispatchers.IO) {
            webApi.getPokemonService().getAllPokemons()
        }
        Log.d(TAG, "getAllPokemons() pokemonList.count: ${pokemonList.count}")
        sendEventHideLoading()
    }

    private fun getPokemonByValue() {
        Log.d(TAG, "getPokemonByValue()")
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

    private fun sendEventErrorInvalidInput() {
        Log.d(TAG, "sendEventErrorInvalidInput()")
        val errorEvent = OnSearchErrorEvent(errorMessageInputInvalid)
        eventApi.publish(errorEvent)
    }

    private fun sendEventErrorGeneric() {
        Log.d(TAG, "sendEventErrorGeneric()")
        eventApi.publish(OnSearchErrorEvent(errorMessageGeneric))
    }

    private fun sendEventShowLoading() {
        Log.d(TAG, "sendEventShowLoading()")
        eventApi.publish(OnLoadingEvent(true))
    }

    private fun sendEventHideLoading() {
        Log.d(TAG, "sendEventHideLoading()")
        eventApi.publish(OnLoadingEvent(false))
    }
}