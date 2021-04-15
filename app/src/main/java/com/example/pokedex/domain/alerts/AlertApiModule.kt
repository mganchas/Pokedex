package com.example.pokedex.domain.alerts

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlertApiModule  {
    @Provides
    fun provideAlertApi() : IAlertApi = AlertApiToast()
}