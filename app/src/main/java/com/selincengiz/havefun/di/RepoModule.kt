package com.selincengiz.havefun.di

import com.selincengiz.havefun.data.repo.ApiEventRepo
import com.selincengiz.havefun.data.source.remote.EventService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideApiEventRepo(eventService: EventService) =
        ApiEventRepo(eventService)
}