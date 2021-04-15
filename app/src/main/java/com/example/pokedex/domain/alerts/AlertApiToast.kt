package com.example.pokedex.domain.alerts

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.pokedex.R

class AlertApiToast : IAlertApi
{
    companion object {
        private val TAG = AlertApiToast::class.java.simpleName
    }

    override fun showMessage(context: Context, message: String) {
        Log.d(TAG, "showMessage() message: $message")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(context: Context, @StringRes resource: Int) {
        Log.d(TAG, "showMessage()")
        showMessage(context, context.resources.getString(resource))
    }

    override fun showGenericErrorMessage(context: Context) {
        Log.d(TAG, "showGenericErrorMessage()")
        showMessage(context, R.string.alert_generic_error)
    }
}