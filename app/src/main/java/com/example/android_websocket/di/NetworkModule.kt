package com.example.android_websocket.di

import com.example.android_websocket.network.websocket.binary.BinaryWebSocketHandler
import com.example.android_websocket.network.websocket.binary.BinaryWebSocketController
import com.example.android_websocket.network.websocket.text.TextWebSocketController
import com.example.android_websocket.network.websocket.text.TextWebSocketHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(39, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideTextWebSocketListener() = TextWebSocketController()

    @Singleton
    @Provides
    fun provideBinaryWebSocketListener() = BinaryWebSocketController()


    @Singleton
    @Provides
    fun provideTextWebSocketHandler(
        textWebSocketController: TextWebSocketController,
        okHttpClient: OkHttpClient
    ) = TextWebSocketHandler(
            textWebSocketController,
            okHttpClient
        )

    @Singleton
    @Provides
    fun provideBinaryWebSocketHandler(
        binaryWebSocketController: BinaryWebSocketController,
        okHttpClient: OkHttpClient
    ) = BinaryWebSocketHandler(
        binaryWebSocketController,
        okHttpClient
    )

}