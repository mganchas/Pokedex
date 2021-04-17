package com.example.pokedex.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.data.events.BaseEvent
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.image.IImageApi
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

    @Inject
    lateinit var imageApi : IImageApi

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: PokemonListAdapter
    private val vm by viewModels<MainViewModel>()

    private val eventsMapper : Map<EventTypes, (Map<String, Any>?) -> Unit> by lazy {
        mapOf(
            EventTypes.ShowLoading to ::onShowLoading,
            EventTypes.HideLoading to ::onHideLoading,
            EventTypes.SearchNotFound to ::onAlertMessage,
            EventTypes.SearchErrorInvalidInput to ::onAlertMessage,
            EventTypes.SearchErrorGeneric to ::onAlertMessage,
            EventTypes.SearchErrorNetwork to ::onAlertMessage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSearchEditText()
        setSearchButton()
        setPreviousButton()
        setNextButton()
        setLimitsSpinner()
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

    private fun onShowLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onShowLoading()")
        loadingApi.show()
    }

    private fun onHideLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onHideLoading()")
        loadingApi.hide()
    }

    /*
        Note: It is true that this method (onSearchError) centralizes every search error type and
        could have been spread into 'N' (error specific) different methods, to handle each error type.
        But, for simplicity of this exercise, I've put all handlers into one method.
    */
    private fun onAlertMessage(payload: Map<String, Any>?) {
        Log.d(TAG, "onSearchError() payload: $payload")
        if (payload == null) {
            throw NullPointerException("payload cannot be null for search errors")
        }
        val message = payload[EventTypesMapper.MESSAGE]?.toString() ?: throw IndexOutOfBoundsException("key not in map")
        Log.d(TAG, "onSearchError() message: $message")
        alertApi.showMessage(this, message)
    }

    private fun setSearchEditText() {
        Log.d(TAG, "setSearchEditText()")
        binding.searchEditText.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editText: Editable?) {
                val newSearchValue = editText.toString()
                Log.d(TAG, "setSearchEditText().afterTextChanged() newSearchValue: $newSearchValue")
                vm.setSearchValue(newSearchValue)
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun setSearchButton() {
        Log.d(TAG, "setSearchButton()")
        binding.searchButton.setOnClickListener {
            Log.d(TAG, "setSearchButton().onClick()")
            vm.onSearch()
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

    private fun setLimitsSpinner() {
        Log.d(TAG, "setLimitsSpinner()")

        ArrayAdapter.createFromResource(this, R.array.api_limits, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.limitsSpinner.adapter = adapter
        }
        binding.limitsSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                Log.d(TAG, "setLimitsSpinner().onItemSelected()")
                val selectedLimit = adapterView.getItemAtPosition(position).toString().toInt()
                Log.d(TAG, "setLimitsSpinner().onItemSelected() selectedLimit: $selectedLimit")
                vm.onNewLimit(selectedLimit)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                Log.d(TAG, "setLimitsSpinner().onNothingSelected()")
            }
        }
    }

    private fun setListAdapter() {
        Log.d(TAG, "setListAdapter()")
        listAdapter = PokemonListAdapter(this, imageApi) {
            vm.onPokemonDetail()
        }
    }

    private fun setRecyclerView() {
        Log.d(TAG, "setRecyclerView()")
        binding.pokemonsRecyclerView.apply {
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
        registerPreviousNavigationButtonEnabledObserver()
        registerNextNavigationButtonEnabledObserver()
        registerPokemonCountObserver()
    }

    private fun registerListObserver() {
        Log.d(TAG, "registerListObserver()")
        vm.pokemonSearch.observe(this, {
            Log.d(TAG, "registerObservers().onObserve() list: $it")
            when(it) {
                null -> clearList()
                else -> updateList(it.results)
            }
        })
    }

    private fun clearList() {
        Log.d(TAG, "clearList()")
        listAdapter.clearData()
    }

    private fun updateList(list : List<Pokemon>) {
        Log.d(TAG, "updateList() list.size: ${list.size}")
        listAdapter.setData(list)
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

    private fun registerPokemonCountObserver() {
        Log.d(TAG, "registerPokemonCountObserver()")
        vm.pokemonsCount.observe(this, {
            Log.d(TAG, "registerPokemonCountObserver().onObserve() value: $it")
            binding.pokemonsCountTextView.text = getString(R.string.label_pokemons_count, it.toString())
        })
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterListObserver()
        unregisterPreviousNavigationButtonEnabledObserver()
        unregisterNextNavigationButtonEnabledObserver()
        unregisterPokemonsCountObserver()
    }

    private fun unregisterListObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.pokemonSearch.removeObservers(this)
    }

    private fun unregisterPreviousNavigationButtonEnabledObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.isPreviousNavigationButtonEnabled.removeObservers(this)
    }

    private fun unregisterNextNavigationButtonEnabledObserver() {
        Log.d(TAG, "unregisterListObserver()")
        vm.isNextNavigationButtonEnabled.removeObservers(this)
    }

    private fun unregisterPokemonsCountObserver() {
        Log.d(TAG, "unregisterPokemonsCountObserver()")
        vm.pokemonsCount.removeObservers(this)
    }
}