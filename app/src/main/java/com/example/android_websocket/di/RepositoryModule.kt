package com.example.android_websocket.di

import com.example.android_websocket.network.websocket.binary.BinaryWebSocketHandler
import com.example.android_websocket.network.websocket.text.TextWebSocketHandler
import com.example.android_websocket.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        provideTextWebSocketHandler: TextWebSocketHandler,
        provideBinaryWebSocketHandler: BinaryWebSocketHandler
    ): MainRepository {
        return MainRepository(
            provideTextWebSocketHandler,
            provideBinaryWebSocketHandler
        )
    }
}