package com.example.pokedex.domain.repository

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryApiModule {
    @Provides
    fun provideRepositoryApi(@ApplicationContext appContext: Context) : IRepositoryApi = RepositoryApiRetrofit(appContext)
}