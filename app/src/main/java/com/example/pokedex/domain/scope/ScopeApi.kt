package com.example.pokedex.domain.scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object ScopeApi
{
    fun io() = CoroutineScope(Dispatchers.IO)
    fun main() = CoroutineScope(Dispatchers.Main)
    fun default() = CoroutineScope(Dispatchers.Default)
}