package com.example.pokedex.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.databinding.RecyclerviewPokemonItemBinding
import com.example.pokedex.domain.scope.ScopeApi
import kotlinx.coroutines.launch

class PokemonListAdapter(
    context: Context,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pokemons = mutableListOf<Pokemon>()

    inner class PokemonViewHolder(private val binding : RecyclerviewPokemonItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(pokemon: Pokemon)
        {
            binding.apply {
                pokemonId.text = pokemon.id.toString()
                pokemonName.text = pokemon.name
                //pokemonImage.setImageBitmap(pokemon.image)
                pokemonImage.setImageResource(R.drawable.ic_pokeball)
                parentLayout.setOnClickListener {
                    onItemClick.invoke(pokemon)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = RecyclerviewPokemonItemBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PokemonViewHolder).bind(pokemons[position])
    }

    override fun getItemCount() = pokemons.size

    fun setData(pokemonList: List<Pokemon>) {
        // to prevent racing conditions
        synchronized(this) {
            pokemons.clear()
            pokemons.addAll(pokemonList)
            updateView()
        }
    }

    private fun updateView() = ScopeApi.main().launch {
        notifyDataSetChanged()
    }
}