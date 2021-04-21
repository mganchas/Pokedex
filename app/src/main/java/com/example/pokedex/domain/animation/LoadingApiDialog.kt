package com.example.pokedex.domain.animation

import android.app.Dialog
import android.content.Context
import android.util.Log
import com.example.pokedex.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LoadingApiDialog @Inject constructor(
    @ActivityContext private val activityContext: Context
) : ILoadingApi
{
    companion object {
        private val TAG = LoadingApiDialog::class.java.simpleName
    }

    private lateinit var dialog : Dialog

    override fun show() {
        Log.d(TAG, "show()")
        dialog = Dialog(activityContext).apply {
            setContentView(R.layout.dialog_loading)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    override fun hide() {
        Log.d(TAG, "hide()")
        dialog.dismiss()
    }
}