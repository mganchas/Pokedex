package com.example.pokedex.data.extensions

import android.content.Context
import android.util.TypedValue

fun Float.toDP(context: Context) : Int
{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    ).toInt()
}