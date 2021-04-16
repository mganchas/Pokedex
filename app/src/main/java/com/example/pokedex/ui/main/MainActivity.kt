package com.example.pokedex.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.data.events.BaseEvent
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.loading.ILoadingApi
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
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

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityMainBinding

    private val vm by viewModels<MainViewModel>()

    private lateinit var listAdapter: PokemonListAdapter

    private val eventsMapper : Map<EventTypes, (Map<String, Any>?) -> Unit> by lazy {
        mapOf(
            EventTypes.ShowLoading to ::onShowLoading,
            EventTypes.HideLoading to ::onHideLoading,
            EventTypes.SearchErrorInvalidInput to ::onSearchError,
            EventTypes.SearchErrorGeneric to ::onSearchError,
            EventTypes.SearchErrorNetwork to ::onSearchError
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSearchButton()
        setPreviousButton()
        setNextButton()
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

    @Subscribe
    fun onEvent(event : BaseEvent) {
        Log.d(TAG, "onEvent() eventType: ${event.eventType} | payload: ${event.payload}")
        try {
            val eventMapperFunction = eventsMapper[event.eventType] ?: throw NullPointerException("invalid event type")
            eventMapperFunction(event.payload)
        } catch (e : Exception) {

        }
    }

    /*
        Note: It is true that this method (onSearchError) centralizes every search error type and
        could have been spread into 'N' (error specific) different methods, to handle each error type.
        But, for simplicity of this exercise, I've put all handlers into one method.
    */
    private fun onSearchError(payload: Map<String, Any>?) {
        Log.d(TAG, "onSearchError() payload: $payload")
        if (payload == null) {
            throw NullPointerException("payload cannot be null for search errors")
        }
        val message = payload[EventTypesMapper.MESSAGE]?.toString() ?: throw IndexOutOfBoundsException("key not in map")
        Log.d(TAG, "onSearchError() message: $message")
        alertApi.showMessage(this, message)
    }

    private fun onShowLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onShowLoading()")
        loadingApi.show()
    }

    private fun onHideLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onHideLoading()")
        loadingApi.hide()
    }

    private fun setSearchButton() {
        Log.d(TAG, "setSearchButton()")
        binding.searchButton.setOnClickListener {
            val inputValue = binding.editSearch.text.toString()
            Log.d(TAG, "setSearchButton().onClick() inputValue: $inputValue")
            vm.onSearch(inputValue)
        }
    }

    private fun setPreviousButton() {
        Log.d(TAG, "setPreviousButton()")
        binding.previousPageButton.setOnClickListener {
            Log.d(TAG, "setPreviousButton().onClick()")
            vm.onPrevious()
        }
    }

    private fun setNextButton() {
        Log.d(TAG, "setNextButton()")
        binding.nextPageButton.setOnClickListener {
            Log.d(TAG, "setNextButton().onClick()")
            vm.onNext()
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
            when(it) {
                true -> binding.previousPageButton.setEnabled()
                false -> binding.previousPageButton.setDisabled()
            }
        })
    }

    private fun registerNextNavigationButtonEnabledObserver() {
        Log.d(TAG, "registerNextNavigationButtonEnabledObserver()")
        vm.isNextNavigationButtonEnabled.observe(this, {
            Log.d(TAG, "registerNextNavigationButtonEnabledObserver().onObserve() value: $it")
            binding.nextPageButton.isEnabled = it
            when(it) {
                true -> binding.nextPageButton.setEnabled()
                false -> binding.nextPageButton.setDisabled()
            }
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