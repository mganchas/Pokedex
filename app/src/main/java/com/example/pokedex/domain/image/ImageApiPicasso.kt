package com.example.pokedex.domain.image

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ImageApiPicasso @Inject constructor() : IImageApi
{
    companion object {
        private val TAG = ImageApiPicasso::class.java.simpleName
    }

    override fun loadImageFromUrlIntoView(url: String, view: ImageView) {
        Log.d(TAG, "loadImageFromUrlIntoView() url: $url")
        Picasso.get().load(url).into(view)
    }
}