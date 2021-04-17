package com.example.pokedex.domain.image

import android.widget.ImageView

interface IImageApi {
    fun loadImageFromUrlIntoView(url: String, view: ImageView)
}