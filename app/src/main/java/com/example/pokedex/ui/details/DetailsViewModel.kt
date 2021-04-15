package com.example.pokedex.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pokedex.domain.image.ImageApiPicasso
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val handle: SavedStateHandle,
    private val imageApi: ImageApiPicasso
) : ViewModel()
{

}