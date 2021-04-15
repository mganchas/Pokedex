package com.example.pokedex.domain.image

import android.util.Log
import javax.inject.Inject

class ImageApiPicasso @Inject constructor() : IImageApi
{
    companion object {
        private val TAG = ImageApiPicasso::class.java.simpleName
    }

    override fun loadImageFromUrl(url: String) {
        Log.d(TAG, "loadImageFromUrl() url: $url")
    }
}