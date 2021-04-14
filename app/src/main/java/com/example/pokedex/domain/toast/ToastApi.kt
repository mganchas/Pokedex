package com.example.pokedex.domain.toast

import android.content.Context
import android.widget.Toast
import com.example.pokedex.R

class ToastApi
{
    fun showShortToast(context: Context, resource : Int) {
        showShortToast(context, context.resources.getString(resource))
    }

    fun showShortToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, resource : Int) {
        showLongToast(context, context.resources.getString(resource))
    }

    fun showLongToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showGenericErrorToast(context: Context) {
        showLongToast(context, R.string.toast_generic_error)
    }
}