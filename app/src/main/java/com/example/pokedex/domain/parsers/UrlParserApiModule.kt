package com.example.pokedex.domain.parsers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UrlParserApiModule {
    @Provides
    fun provideUrlParserApi() : IUrlParserApi = UrlParserApi()
}