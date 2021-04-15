package com.example.pokedex.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.events.OnSearchError
import com.example.pokedex.data.types.SearchErrorTypes
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.image.IImageApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val state: SavedStateHandle,
    val imageApi: IImageApi,
    val eventApi : IEventApi,
) : ViewModel()
{
    fun onSearch() {
        val errorType = SearchErrorTypes.InputEmpty

        /*
        val message = when(errorType) {
            SearchErrorTypes.InputEmpty -> application.getString(R.string.alert_input_invalid)
        }
        */
        val message = "ola"

        val errorEvent = OnSearchError(message)
        eventApi.publish(errorEvent)
    }
}