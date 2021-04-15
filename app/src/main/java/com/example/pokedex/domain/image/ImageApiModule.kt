package com.example.pokedex.domain.image

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ImageApiModule {
    @Provides
    fun provideImageApi() : IImageApi = ImageApiPicasso()
}