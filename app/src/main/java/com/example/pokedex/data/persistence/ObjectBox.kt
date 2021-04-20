package com.example.pokedex.data.persistence

import android.content.Context
import com.example.pokedex.data.models.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox
{
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}