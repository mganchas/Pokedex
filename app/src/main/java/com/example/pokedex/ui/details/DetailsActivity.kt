package com.example.pokedex.ui.details

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.data.model.events.BaseEvent
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.model.pokemon.Pokemon
import com.example.pokedex.data.model.pokemon.stats.PokemonStatPair
import com.example.pokedex.data.types.EventTypes
import com.example.pokedex.databinding.ActivityDetailsBinding
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.animation.ILoadingApi
import com.example.pokedex.domain.events.IEventApi
import com.example.pokedex.domain.image.IImageApi
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity()
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
        private val TAG = DetailsActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var pokemonStatsListAdapter: PokemonStatsListAdapter
    private val vm: DetailsViewModel by viewModels()

    private val eventsMapper : Map<EventTypes, (Map<String, Any>?) -> Unit> by lazy {
        mapOf(
            EventTypes.ErrorNetwork to ::onAlertMessage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pokemon = getIncomingPokemon() ?: throw NullPointerException("pokemon missing")
        setToolbar()
        setSpritesCarousel()
        setRecyclerView { setStatsAdapter() }
        setFavouriteButton()
        vm.initWithPokemon(pokemon)
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
            e.printStackTrace()
            runOnUiThread {
                alertApi.showGenericErrorMessage(this)
            }
        }
    }

    private fun getIncomingPokemon() : Pokemon? {
        Log.d(TAG, "getIncomingPokemon()")
        return intent.getSerializableExtra(EventTypesMapper.NAVIGATION_DATA) as Pokemon?
    }

    private fun setStatsAdapter() {
        Log.d(TAG, "setStatsAdapter()")
        pokemonStatsListAdapter = PokemonStatsListAdapter(application)
    }

    private fun setToolbar() {
        Log.d(TAG, "setToolbar()")
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun setSpritesCarousel() {
        Log.d(TAG, "setSpritesCarousel()")
        binding.spritesCarouselView.setImageListener { position, imageView ->
            Log.d(TAG, "setSpritesCarousel().onImage() position: $position")
            val sprites = vm.sprites.value ?: return@setImageListener
            imageApi.loadImageFromUrlIntoView(sprites[position], imageView)
        }
    }

    /*
        Note: adding some temporal coupling (recyclerView needs the adapter set up prior to
        its own setting)
    */
    private fun setRecyclerView(functionDependency : () -> Unit) {
        Log.d(TAG, "setRecyclerView()")
        functionDependency()
        binding.pokemonDataRecyclerView.apply {
            adapter = pokemonStatsListAdapter
            layoutManager = LinearLayoutManager(this@DetailsActivity)
            setHasFixedSize(true)
        }
    }

    private fun setFavouriteButton() {
        Log.d(TAG, "setFavouriteButton()")
        binding.favouriteContainer.setOnClickListener {
            Log.d(TAG, "setFavouriteButton().onClick()")
            binding.animation.playAnimation()
            vm.setAsFavourite()
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
        registerStatsObserver()
        registerSpritesObserver()
        registerPokemonObserver()
    }

    private fun registerStatsObserver() {
        Log.d(TAG, "registerStatsObserver()")
        vm.stats.observe(this, {
            Log.d(TAG, "registerStatsObserver().onObserve() value: $it")
            when(it) {
                null -> clearStats()
                else -> updateStats(it)
            }
        })
    }

    private fun clearStats() {
        Log.d(TAG, "clearStats()")
        pokemonStatsListAdapter.clearData()
    }

    private fun updateStats(value : List<PokemonStatPair>) {
        Log.d(TAG, "updateStats() value.size: ${value.size}")
        pokemonStatsListAdapter.setData(value)
    }

    private fun registerSpritesObserver() {
        Log.d(TAG, "registerSpritesObserver()")
        vm.sprites.observe(this, {
            Log.d(TAG, "registerSpritesObserver().onObserve() value: $it")
            val sprites = it ?: throw NullPointerException("sprites cannot be null")
            binding.spritesCarouselView.pageCount = sprites.size
        })
    }

    private fun registerPokemonObserver() {
        Log.d(TAG, "registerPokemonObserver()")
        vm.pokemon.observe(this, {
            Log.d(TAG, "registerPokemonObserver().onObserve() value: $it")
            binding.pokemonName.text = it.name
            binding.toolbar.title = it.name
        })
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterStatsObserver()
        unregisterSpritesObserver()
        unregisterPokemonObserver()
    }

    private fun unregisterStatsObserver() {
        Log.d(TAG, "unregisterStatsObserver()")
        vm.stats.removeObservers(this)
    }

    private fun unregisterSpritesObserver() {
        Log.d(TAG, "unregisterSpritesObserver()")
        vm.sprites.removeObservers(this)
    }

    private fun unregisterPokemonObserver() {
        Log.d(TAG, "unregisterPokemonNameObserver()")
        vm.pokemon.removeObservers(this)
    }

    /*
        Note: It is true that this method (onSearchError) centralizes every search error type and
        could have been spread into 'N' (error specific) different methods, to handle each error type.
        But, for simplicity of this exercise, I've put all handlers into one method.
    */
    private fun onAlertMessage(payload: Map<String, Any>?) {
        Log.d(TAG, "onAlertMessage() payload: $payload")
        if (payload == null) {
            throw NullPointerException("payload cannot be null for search errors")
        }
        val message = payload[EventTypesMapper.MESSAGE]?.toString() ?: throw IndexOutOfBoundsException("key not in map")
        Log.d(TAG, "onAlertMessage() message: $message")
        runOnUiThread {
            alertApi.showMessage(this, message)
        }
    }
}