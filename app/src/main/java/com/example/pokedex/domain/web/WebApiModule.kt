package com.example.pokedex.domain.web

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WebApiModule {
    @Provides
    fun provideWebApi(@ApplicationContext appContext: Context) : IWebApi = WebApiRetrofit(appContext)
}