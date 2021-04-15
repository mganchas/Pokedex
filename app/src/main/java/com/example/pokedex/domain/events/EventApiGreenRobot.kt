package com.example.pokedex.domain.events

import android.util.Log
import com.example.pokedex.data.events.BaseEvent
import org.greenrobot.eventbus.EventBus

class EventApiGreenRobot : IEventApi
{
    companion object {
        private val TAG = EventApiGreenRobot::class.java.simpleName
    }

    override fun publish(event: BaseEvent) {
        Log.d(TAG, "publish() event: $event")
        EventBus.getDefault().post(event)
    }

    override fun register(component: Any) {
        Log.d(TAG, "register() component: ${component.javaClass.simpleName}")
        EventBus.getDefault().register(component)
    }

    override fun unregister(component: Any) {
        Log.d(TAG, "unregister() component: ${component.javaClass.simpleName}")
        EventBus.getDefault().unregister(component)
    }
}