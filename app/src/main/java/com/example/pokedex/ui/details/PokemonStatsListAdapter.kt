package com.example.pokedex.ui.details

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.models.stats.PokemonStatPair
import com.example.pokedex.databinding.PokemonStatsItemBinding
import com.example.pokedex.domain.scope.ScopeApi
import kotlinx.coroutines.launch


class PokemonStatsListAdapter(
    // this is not advisable if it's for Activity/Fragment (I'm using the app context to prevent memory leaks)
    private val appContext: Application,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(appContext)
    private var stats = mutableListOf<PokemonStatPair>()

    inner class PokemonStatViewHolder(private val binding: PokemonStatsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemonStat: PokemonStatPair) {
            binding.apply {
                pokemonStatValue.text = pokemonStat.value.toString()
                pokemonStatName.text = pokemonStat.details.getName(appContext)
                pokemonStatsAbilityContainer.background.level = pokemonStat.details.getColorLevel()
                pokemonStat.details.getIcon(appContext).let {
                    pokemonStatsAbilityIcon.setImageDrawable(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PokemonStatsItemBinding.inflate(inflater, parent, false)
        return PokemonStatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PokemonStatViewHolder).bind(stats[position])
    }

    override fun getItemCount() = stats.size

    fun clearData() {
        stats.clear()
    }

    fun setData(statsList: List<PokemonStatPair>) {
        // to prevent racing conditions
        synchronized(this) {
            clearData()
            stats.addAll(statsList)
            updateView()
        }
    }

    private fun updateView() = ScopeApi.main().launch {
        notifyDataSetChanged()
    }
}