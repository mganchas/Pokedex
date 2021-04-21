package com.example.pokedex.data.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class LinkedListExtensionsTest
{
    private lateinit var list : LinkedList<String>

    @Before
    fun setup() {
        list = LinkedList<String>()
        list.add("a")
        list.add("b")
    }

    @Test
    fun addIfNotNull_receivesNullValue_doesNotAddToLinkedList() {
        val initialListSize = list.size
        list.addIfNotNull(null)
        Assert.assertEquals(initialListSize, list.size)
    }

    @Test
    fun addIfNotNull_receivesNonNullValue_addsToLinkedList() {
        val initialListSize = list.size
        list.addIfNotNull("c")
        Assert.assertTrue(list.size > initialListSize)
    }

    @After
    fun cleanup() {
        list.clear()
    }
}