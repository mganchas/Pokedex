package com.example.pokedex.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.databinding.PokemonSearchItemBinding
import com.example.pokedex.domain.image.IImageApi
import com.example.pokedex.domain.scope.ScopeApi
import kotlinx.coroutines.launch

class PokemonListAdapter(
    context: Context,
    private val imageApi : IImageApi,
    private val onItemClick: (PokemonDetails) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pokemons = mutableListOf<PokemonDetails>()

    inner class PokemonViewHolder(private val binding : PokemonSearchItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(pokemon: PokemonDetails)
        {
            binding.apply {
                pokemonId.text = pokemon.pokemonId.toString()
                pokemonName.text = pokemon.name
                pokemon.sprites.target.frontShiny?.let {
                    imageApi.loadImageFromUrlIntoView(it, pokemonImage)
                }
                parentLayout.setOnClickListener {
                    onItemClick.invoke(pokemon)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = PokemonSearchItemBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PokemonViewHolder).bind(pokemons[position])
    }

    override fun getItemCount() = pokemons.size

    fun clearData() {
        pokemons.clear()
        updateView()
    }

    fun setData(pokemonList: List<PokemonDetails>) {
        // to prevent racing conditions
        synchronized(this) {
            clearData()
            pokemons.addAll(pokemonList)
            updateView()
        }
    }

    private fun updateView() = ScopeApi.main().launch {
        notifyDataSetChanged()
    }
}