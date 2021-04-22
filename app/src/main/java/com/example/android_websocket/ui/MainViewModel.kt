package com.example.android_websocket.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.android_websocket.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class MainViewModel
    @Inject constructor (
        private val mainRepository: MainRepository,
    ) : ViewModel() {

    private val TAG = "AppDebug: MainViewModel"

    fun subscribeTextWebSocket() = mainRepository.getTextWebSocketSubscription()
    fun subscribeBinaryWebSocket() = mainRepository.getBinaryWebSocketSubscription()



    fun sendMessage(message: String) = mainRepository.sendText(message)

    fun reconnect() = mainRepository.reconnect()

    fun getSentImageCount() = mainRepository.getSentImageCount()
    fun getSentTextCount() = mainRepository.getSentTextCount()

    fun sendColor(color: Int): Bitmap = mainRepository.sendColor(color)


}