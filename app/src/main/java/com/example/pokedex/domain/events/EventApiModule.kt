package com.example.pokedex.domain.events

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EventApiModule  {
    @Provides
    fun provideEventApi() : IEventApi = EventApiGreenRobot()
}