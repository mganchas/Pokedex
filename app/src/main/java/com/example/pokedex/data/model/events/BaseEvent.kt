package com.example.pokedex.data.model.events

import com.example.pokedex.data.types.EventTypes

data class BaseEvent(
    val eventType : EventTypes,
    val payload: Map<String, Any>? = null
)
