package com.example.pokedex.domain.animation

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object LoadingApiModule {
    @Provides
    fun provideLoadingApi(@ActivityContext activityContext: Context) : ILoadingApi = LoadingApiDialog(activityContext)
}