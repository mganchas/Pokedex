package com.example.pokedex.data.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.data.model.pokemon.PokemonSprites
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonSpritesExtenstionsTest {
    private lateinit var pokemonSpritesAllFilled: PokemonSprites
    private lateinit var pokemonSpritesAllNull: PokemonSprites
    private lateinit var pokemonSpritesOneFilled: PokemonSprites
    private lateinit var pokemonSpritesDifferentFilledValues: PokemonSprites
    private val dummyFillValue = "dummyValue"
    private val otherDummyFillValue = "otherDummyValue"

    @Before
    fun setup() {
        pokemonSpritesAllFilled = PokemonSprites(
            frontShiny =  dummyFillValue,
            backShiny =  dummyFillValue,
            frontDefault =  dummyFillValue,
            backDefault = dummyFillValue,
            frontFemale =  dummyFillValue,
            backFemale = dummyFillValue,
            frontShinyFemale =  dummyFillValue,
            backShinyFemale =  dummyFillValue,
        )

        pokemonSpritesAllNull = PokemonSprites(
            frontShiny =  null,
            backShiny =  null,
            frontDefault =  null,
            backDefault = null,
            frontFemale =  null,
            backFemale = null,
            frontShinyFemale =  null,
            backShinyFemale =  null,
        )

        pokemonSpritesOneFilled = PokemonSprites(
            frontShiny =  dummyFillValue,
            backShiny =  null,
            frontDefault =  null,
            backDefault = null,
            frontFemale =  null,
            backFemale = null,
            frontShinyFemale =  null,
            backShinyFemale =  null,
        )

        pokemonSpritesDifferentFilledValues = PokemonSprites(
            frontShiny =  dummyFillValue,
            backShiny =  otherDummyFillValue,
            frontDefault =  null,
            backDefault = null,
            frontFemale =  null,
            backFemale = null,
            frontShinyFemale =  null,
            backShinyFemale =  null,
        )
    }

    @Test
    fun toLinkedList_allFilled_returnsNonEmptyLinkedList() {
        Assert.assertTrue(pokemonSpritesAllFilled.toLinkedList().isNotEmpty())
    }

    @Test
    fun toLinkedList_allNull_returnsEmptyLinkedList() {
        Assert.assertTrue(pokemonSpritesAllNull.toLinkedList().isEmpty())
    }

    @Test
    fun toLinkedList_oneFilled_returnsLinkedListWithValue() {
        val list = pokemonSpritesOneFilled.toLinkedList()
        Assert.assertEquals(dummyFillValue, list.first())
    }

    @Test
    fun toLinkedList_oneFilled_returnsLinkedListWithSingleEntry() {
        val list = pokemonSpritesOneFilled.toLinkedList()
        Assert.assertTrue(list.size == 1)
    }

    @Test
    fun toLinkedList_differentFilledValues_returnsLinkedListWithDifferentValues() {
        val list = pokemonSpritesDifferentFilledValues.toLinkedList()
        val firstElement = list.first()
        val secondElement = list.last()
        Assert.assertNotEquals(firstElement, secondElement)
    }

    @Test
    fun toLinkedList_differentFilledValues_returnsLinkedListWithMultipleEntries() {
        val list = pokemonSpritesDifferentFilledValues.toLinkedList()
        Assert.assertTrue(list.size > 1)
    }
}