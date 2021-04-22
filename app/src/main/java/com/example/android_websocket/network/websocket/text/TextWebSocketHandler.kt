package com.example.android_websocket.network.websocket.text

import android.util.Log
import com.example.android_websocket.network.websocket.binary.BinaryWebSocketController
import com.example.android_websocket.util.WEBSOCKET_ADDRESS_TEXT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class TextWebSocketHandler(
    private val textWebSocketController: TextWebSocketController,
    private val okHttpClient: OkHttpClient
) {
    private val TAG = "AppDebug: TextWebSocketHandler"

    private var webSocket: WebSocket? = null

    private var sentTextCounter = 0

    private val _stateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private var socketIsOpen = false

    init {
        Log.d(TAG, "init: Starting new text WebSocket handler")
        webSocket = okHttpClient.newWebSocket(
                Request.Builder()
                        .url(WEBSOCKET_ADDRESS_TEXT)
                        .build(),
            textWebSocketController
        )

        GlobalScope.launch(Dispatchers.IO) {
            textWebSocketController.sharedFlow.collect {
                when(it) {
                    is TextWebSocketController.TextWebSocketEvent.None -> {
                        //Nothing
                    }
                    is TextWebSocketController.TextWebSocketEvent.MessagedText -> {
                        //Nothing
                    }
                    is TextWebSocketController.TextWebSocketEvent.Failed -> {
                        socketIsOpen = false
                    }
                    is TextWebSocketController.TextWebSocketEvent.Opened -> {
                        socketIsOpen = true
                    }
                    is TextWebSocketController.TextWebSocketEvent.Closed -> {
                        socketIsOpen = false
                    }
                }
            }
        }
    }

    fun reconnect() {
        closeSocket()
        startNewSocket()
    }

    fun closeSocket() {
        Log.d(TAG, "closeSocket: Closing text socket.")
        webSocket?.close(1000, "Normal Exit")
        webSocket = null
    }

    fun startNewSocket() {
        Log.d(TAG, "startNewSocket: Starting text socket.")
        webSocket = okHttpClient.newWebSocket(
            Request.Builder()
                    .url(WEBSOCKET_ADDRESS_TEXT)
                    .build(),
            textWebSocketController
        )
    }

    fun send(text: String) {
        webSocket?.let {
            if(!socketIsOpen) {
                Log.d(TAG, "send: Can not sent on a closed socket!")
                return
            }

            if(it.send(text)){
                _stateFlow.value = ++sentTextCounter
            }
        }
    }

    fun subscribeWebSocket() = textWebSocketController.sharedFlow
    fun subscribeCounter() = _stateFlow
}