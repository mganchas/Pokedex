package com.example.pokedex.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.pokedex.R
import com.example.pokedex.data.types.PokemonStats
import com.example.pokedex.databinding.PokemonStatsItemBinding

class PokemonStatsItem(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    var image: Drawable?
        private set

    var name : String?
        private set

    var value : String?
        private set

    private var backColorLevel : Int = PokemonStats.HP.ordinal

    private var binding : PokemonStatsItemBinding

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PokemonStatsItem,
            0,
            0
        ).apply {
            try {
                backColorLevel = getInteger(R.styleable.PokemonStatsItem_statBackColor, PokemonStats.HP.ordinal)
                image = getDrawable(R.styleable.PokemonStatsItem_statImage)
                name = getString(R.styleable.PokemonStatsItem_statName)
                value = getString(R.styleable.PokemonStatsItem_statValue)
            }
            finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = PokemonStatsItemBinding.inflate(inflater, this, true)

        initContainer()
        initIcon()
        initName()
        initValue()
        updateView()
    }

    private fun initContainer() {
        binding.pokemonStatsAbilityContainer.background.level = backColorLevel
    }

    private fun initIcon() {
        binding.pokemonStatsAbilityIcon.setImageDrawable(image)
    }

    private fun initName() {
        binding.pokemonStatName.text = name
    }

    private fun initValue() {
        binding.pokemonStatValue.text = value
    }

    private fun updateView() {
        invalidate()
        requestLayout()
    }
}