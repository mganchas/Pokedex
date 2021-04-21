package com.example.pokedex.data.model

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pokedex.R
import com.example.pokedex.data.model.pokemon.stats.PokemonStatDetailsAttack
import com.example.pokedex.data.types.PokemonStatTypes
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonStatDetailsAttackTest
{
    private lateinit var name : String
    private val colorLevel = PokemonStatTypes.Attack.ordinal
    private lateinit var context : Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        name = context.getString(R.string.stat_attack)
    }

    @Test
    fun getName_returnsCorrectName() {
        Assert.assertEquals(name, PokemonStatDetailsAttack().getName(context))
    }

    @Test
    fun getColorLevel_returnsCorrectColorLevel() {
        Assert.assertEquals(colorLevel, PokemonStatDetailsAttack().getColorLevel())
    }
}