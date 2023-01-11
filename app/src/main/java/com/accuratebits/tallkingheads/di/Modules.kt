package com.accuratebits.tallkingheads.di

import com.accuratebits.tallkingheads.data.DefaultTalksRepository
import com.accuratebits.tallkingheads.data.RandomTalkClient
import com.accuratebits.tallkingheads.data.TalkClient
import com.accuratebits.tallkingheads.data.TalkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UUIDGenerator

@Module
@InstallIn(SingletonComponent::class)
object GeneratorsModule {
    @Provides
    @UUIDGenerator
    fun providesUUIDGenerator(): () -> UUID = { UUID.randomUUID() }
}


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTalksRepository(): TalkRepository {
        return DefaultTalksRepository()
    }

    @Singleton
    @Provides
    fun provideTalksClient(): TalkClient {
        return RandomTalkClient()
    }
}