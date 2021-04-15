package com.example.pokedex.domain.alerts

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.pokedex.R

class AlertApiToast : IAlertApi
{
    override fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(context: Context, @StringRes resource: Int) {
        showMessage(context, context.resources.getString(resource))
    }

    override fun showGenericErrorMessage(context: Context) {
        showMessage(context, R.string.alert_generic_error)
    }
}