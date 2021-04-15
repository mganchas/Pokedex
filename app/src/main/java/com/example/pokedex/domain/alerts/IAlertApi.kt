package com.example.pokedex.domain.alerts

import android.content.Context
import androidx.annotation.StringRes

interface IAlertApi {
    fun showMessage(context: Context, message : String)
    fun showMessage(context: Context, @StringRes resource : Int)
    fun showGenericErrorMessage(context: Context)
}