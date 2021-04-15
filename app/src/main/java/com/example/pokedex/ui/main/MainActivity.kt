package com.example.pokedex.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.data.events.OnLoadingEvent
import com.example.pokedex.data.events.OnSearchErrorEvent
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.loading.ILoadingApi
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var eventApi : IEventApi

    @Inject
    lateinit var loadingApi : ILoadingApi

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()

    private lateinit var listAdapter: PokemonListAdapter

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSearchButton()
        setListAdapter()
        setRecyclerView()
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        registerEvents()
        registerObservers()
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
        unregisterEvents()
        unregisterObservers()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchError(onSearchErrorEvent: OnSearchErrorEvent) {
        Log.d(TAG, "onSearchError() onSearchError.message: ${onSearchErrorEvent.message}")
        alertApi.showMessage(this, onSearchErrorEvent.message)
    }

    @Subscribe
    fun onLoading(onLoadingEvent: OnLoadingEvent) {
        Log.d(TAG, "onLoading() onLoadingEvent.visible: ${onLoadingEvent.visible}")
        when(onLoadingEvent.visible) {
            true -> loadingApi.show()
            false -> loadingApi.hide()
        }
    }

    private fun setSearchButton() {
        Log.d(TAG, "setSearchButton()")
        binding.searchButton.setOnClickListener {
            val inputValue = binding.editSearch.text.toString()
            Log.d(TAG, "setSearchButton().onClick() inputValue: $inputValue")
            vm.onSearch(inputValue)
        }
    }

    private fun setListAdapter() {
        Log.d(TAG, "setListAdapter()")
        listAdapter = PokemonListAdapter(this) {
            vm.onPokemonDetail()
        }
    }

    private fun setRecyclerView() {
        Log.d(TAG, "setRecyclerView()")
        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun registerEvents() {
        Log.d(TAG, "registerEvents()")
        eventApi.register(this)
    }

    private fun unregisterEvents() {
        Log.d(TAG, "unregisterEvents()")
        eventApi.unregister(this)
    }

    private fun registerObservers() {
        Log.d(TAG, "registerObservers()")
        registerListObserver()
        registerNavigationButtonsVisibilityObserver()
        registerPreviousNavigationButtonEnabledObserver()
        registerNextNavigationButtonEnabledObserver()
    }

    private fun registerListObserver() {
        Log.d(TAG, "registerListObserver()")
        vm.pokemons.observe(this, {
            Log.d(TAG, "registerObservers().onObserve() it.size: ${it.size}")
            updateList(it)
        })
    }

    private fun registerNavigationButtonsVisibilityObserver() {
        Log.d(TAG, "registerNavigationButtonsVisibilityObserver()")
        vm.areNavigationButtonsVisible.observe(this, {
            Log.d(TAG, "registerNavigationButtonsVisibilityObserver().onObserve() value: $it")
            binding.buttonsLayout.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun registerPreviousNavigationButtonEnabledObserver() {
        Log.d(TAG, "registerPreviousNavigationButtonEnabledObserver()")
        vm.isPreviousNavigationButtonEnabled.observe(this, {
            Log.d(TAG, "registerPreviousNavigationButtonEnabledObserver().onObserve() value: $it")
            binding.previousPageButton.isEnabled = it
        })
    }

    private fun registerNextNavigationButtonEnabledObserver() {
        Log.d(TAG, "registerNextNavigationButtonEnabledObserver()")
        vm.isNextNavigationButtonEnabled.observe(this, {
            Log.d(TAG, "registerNextNavigationButtonEnabledObserver().onObserve() value: $it")
            binding.nextPageButton.isEnabled = it
        })
    }

    private fun updateList(list : MutableList<Pokemon>) {
        Log.d(TAG, "updateList() list.size: ${list.size}")
        listAdapter.setData(list)
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterListObserver()
        unregisterNavigationButtonsVisibilityObserver()
        unregisterPreviousNavigationButtonEnabledObserver()
        unregisterNextNavigationButtonEnabledObserver()

    }

    private fun unregisterListObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.pokemons.removeObservers(this)
    }

    private fun unregisterNavigationButtonsVisibilityObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.areNavigationButtonsVisible.removeObservers(this)
    }

    private fun unregisterPreviousNavigationButtonEnabledObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.isPreviousNavigationButtonEnabled.removeObservers(this)
    }

    private fun unregisterNextNavigationButtonEnabledObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.isNextNavigationButtonEnabled.removeObservers(this)
    }
}