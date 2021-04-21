package com.example.pokedex.domain.image

import android.content.Context
import android.widget.ImageView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageApiPicassoTest
{
    private val randomImage = "https://img.devrant.com/devrant/rant/r_1368694_YwK2T.jpg"
    private lateinit var context : Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test(expected = IllegalArgumentException::class)
    fun loadImageFromUrlIntoView_receivesEmptyStringUrl_throwsIllegalArgumentException() {
        ImageApiPicasso().loadImageFromUrlIntoView("", ImageView(context))
    }

    @Test(expected = IllegalStateException::class)
    fun loadImageFromUrlIntoView_invokedFromNonUIThread_throwsIllegalStateException() {
        ImageApiPicasso().loadImageFromUrlIntoView(randomImage, ImageView(context))
    }
}