package com.example.pokedex.data.managers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.data.model.pokemon.stats.*
import com.example.pokedex.data.model.pokemon.stats.abstractions.IPokemonStatDetails
import com.example.pokedex.data.types.PokemonStatTypes
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonStatsManagerTest
{
    private val statTypeRawValid = "hp"
    private val statTypeValid = PokemonStatTypes.HP
    private val statTypeRawInvalid = "dummy"
    private lateinit var statTypeRawValuesList : List<String>
    private lateinit var statTypesList : List<PokemonStatTypes>
    private lateinit var statTypesDetailsList : List<Class<out IPokemonStatDetails>>

    @Before
    fun setup() {
        statTypeRawValuesList = listOf(
            "hp",
            "attack",
            "special-attack",
            "defense",
            "special-defense",
            "speed"
        )

        statTypesList = listOf(
            PokemonStatTypes.HP,
            PokemonStatTypes.Attack,
            PokemonStatTypes.SpecialAttack,
            PokemonStatTypes.Defense,
            PokemonStatTypes.SpecialDefense,
            PokemonStatTypes.Speed
        )

        statTypesDetailsList = listOf(
            PokemonStatDetailsHP::class.java,
            PokemonStatDetailsAttack::class.java,
            PokemonStatDetailsSpecialAttack::class.java,
            PokemonStatDetailsDefense::class.java,
            PokemonStatDetailsSpecialDefense::class.java,
            PokemonStatDetailsSpeed::class.java
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun getStatTypeByRawValue_receivesEmptyString_throwsIllegalArgumentException() {
        PokemonStatsManager.getStatTypeByRawValue("")
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getStatTypeByRawValue_receivesInvalidRawStatValue_throwsIndexOutOfBoundsException() {
        PokemonStatsManager.getStatTypeByRawValue(statTypeRawInvalid)
    }

    @Test
    fun getStatTypeByRawValue_receivesValidRawStatValue_returnsMatchingPokemonStatType() {
        val statType = PokemonStatsManager.getStatTypeByRawValue(statTypeRawValid)
        Assert.assertEquals(statTypeValid, statType)
    }

    @Test
    fun getStatTypeByRawValue_receivesAllValidRawStatValues_returnsAllMatchingPokemonStatType() {
        val matchingList = mutableListOf<Boolean>()
        for (i in statTypeRawValuesList.indices) {
            val statType = PokemonStatsManager.getStatTypeByRawValue(statTypeRawValuesList[i])
            matchingList.add(statType == statTypesList[i])
        }
        Assert.assertTrue(matchingList.all { true })
    }

    @Test
    fun getStatDetailsByType_receivesValidStatValue_returnsMatchingIPokemonStatDetails() {
        val statTypeDetailsImplementation = PokemonStatsManager.getStatDetailsByType(statTypeValid)
        Assert.assertTrue(statTypeDetailsImplementation is PokemonStatDetailsHP)
    }

    @Test
    fun getStatDetailsByType_receivesAllValidStatValues_returnsAllMatchingIPokemonStatDetails() {
        val matchingList = mutableListOf<Boolean>()
        for (i in statTypesList.indices) {
            val statType = PokemonStatsManager.getStatDetailsByType(statTypesList[i])
            matchingList.add(statType == statTypesDetailsList[i])
        }
        Assert.assertTrue(matchingList.all { true })
    }
}