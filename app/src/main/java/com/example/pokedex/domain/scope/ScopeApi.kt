package com.example.pokedex.domain.scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ScopeApi
{
    fun ioScope() = CoroutineScope(Dispatchers.IO)
    fun mainScope() = CoroutineScope(Dispatchers.Main)
    fun defaultScope() = CoroutineScope(Dispatchers.Default)
}