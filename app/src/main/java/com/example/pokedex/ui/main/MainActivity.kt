package com.example.pokedex.ui.main

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R
import com.example.pokedex.data.events.OnSearchError
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.ui.components.ButtonCircle
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private val vm by viewModels<MainViewModel>()

    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var eventApi : IEventApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        setSearchButton()
    }

    override fun onStart() {
        super.onStart()
        eventApi.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventApi.unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchError(onSearchError: OnSearchError) {
        alertApi.showMessage(this, onSearchError.message)
    }

    private fun setToolbar() {
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_pokeball)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setSearchButton() {
        findViewById<ButtonCircle>(R.id.searchButton).setOnClickListener {
            vm.onSearch()
        }
    }
}