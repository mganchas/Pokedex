package com.example.pokedex.domain.events

import com.example.pokedex.data.events.BaseEvent
import org.greenrobot.eventbus.EventBus

class EventApiGreenRobot : IEventApi {
    override fun publish(event: BaseEvent) {
        EventBus.getDefault().post(event)
    }

    override fun register(component: Any) {
        EventBus.getDefault().register(component)
    }

    override fun unregister(component: Any) {
        EventBus.getDefault().unregister(component)
    }
}