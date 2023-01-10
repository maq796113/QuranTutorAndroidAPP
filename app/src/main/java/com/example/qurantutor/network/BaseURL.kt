package com.example.qurantutor.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object BaseURL {
    @Provides
    fun providesModelbaseUrl() = "http://4.193.97.134:8080"
}