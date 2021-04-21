package com.example.pokedex.data.extensions

import java.util.*

fun <T> LinkedList<T>.addIfNotNull(value : T?) {
    value?.let {
        this.add(it)
    }
}