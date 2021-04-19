package com.example.pokedex.ui.details

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.data.mappers.EventTypesMapper
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.stats.PokemonStatPair
import com.example.pokedex.databinding.ActivityDetailsBinding
import com.example.pokedex.domain.alerts.IAlertApi
import com.example.pokedex.domain.image.IImageApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity()
{
    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var imageApi : IImageApi

    companion object {
        private val TAG = DetailsActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var pokemonStatsListAdapter: PokemonStatsListAdapter
    private val vm: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pokemon = getIncomingPokemon() ?: throw NullPointerException("pokemon missing")
        setSpritesCarousel()
        setRecyclerView { setStatsAdapter() }
        vm.initWithPokemon(pokemon)
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        registerObservers()
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
        unregisterObservers()
    }

    private fun getIncomingPokemon() : Pokemon? {
        Log.d(TAG, "getIncomingPokemon()")
        return intent.getSerializableExtra(EventTypesMapper.NAVIGATION_DATA) as Pokemon?
    }

    private fun setStatsAdapter() {
        Log.d(TAG, "setStatsAdapter()")
        pokemonStatsListAdapter = PokemonStatsListAdapter(application)
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

    private fun setSpritesCarousel() {
        Log.d(TAG, "setSpritesCarousel()")
        binding.spritesCarouselView.setImageListener { position, imageView ->
            Log.d(TAG, "setSpritesCarousel().onImage() position: $position")
            val sprites = vm.sprites.value ?: return@setImageListener
            imageApi.loadImageFromUrlIntoView(sprites[position], imageView)
        }
    }

    private fun registerObservers() {
        Log.d(TAG, "registerObservers()")
        registerStatsObserver()
        registerSpritesObserver()
        registerPokemonObserver()
    }

    private fun registerStatsObserver() {
        Log.d(TAG, "registerListObserver()")
        vm.stats.observe(this, {
            Log.d(TAG, "registerListObserver().onObserve() value: $it")
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
}