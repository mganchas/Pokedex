package com.example.pokedex.domain.events

import com.example.pokedex.data.model.events.BaseEvent

interface IEventApi {
    fun publish(event: BaseEvent)
    fun register(component: Any)
    fun unregister(component: Any)
}