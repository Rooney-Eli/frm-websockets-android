package com.example.android_websocket.network.websocket.binary

import android.util.Log
import com.example.android_websocket.util.WEBSOCKET_ADDRESS_BINARY
import com.example.android_websocket.util.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString

class BinaryWebSocketHandler(
    private val binaryWebSocketController: BinaryWebSocketController,
    private val okHttpClient: OkHttpClient
) {
    private val TAG = "AppDebug: BinaryWebSocketHandler"

    private var webSocket: WebSocket? = null

    private var sentImageCounter = 0

    private val _stateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private var socketIsOpen = false


    init {
        Log.d(TAG, "init: Starting new binary WebSocket handler")
        webSocket = okHttpClient.newWebSocket(
                Request.Builder()
                        .url(WEBSOCKET_ADDRESS_BINARY)
                        .build(),
                binaryWebSocketController
        )

        GlobalScope.launch(Dispatchers.IO) {
            binaryWebSocketController.sharedFlow.collect {
                when(it) {
                    is BinaryWebSocketController.BinaryWebSocketEvent.None -> {
                        //Nothing
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.MessagedBinary -> {
                        //Nothing
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Failed -> {
                        socketIsOpen = false
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Opened -> {
                        socketIsOpen = true
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Closed -> {
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
        Log.d(TAG, "closeSocket: Closing binary socket.")
        webSocket?.close(1000, "Normal Exit")
        webSocket = null
    }

    fun startNewSocket() {
        Log.d(TAG, "startNewSocket: Starting binary socket.")
        webSocket = okHttpClient.newWebSocket(
            Request.Builder()
                    .url(WEBSOCKET_ADDRESS_BINARY)
                    .build(),
            binaryWebSocketController
        )
    }

    fun send(byteString: ByteString) {
        webSocket?.let {
            if(!socketIsOpen) {
                Log.d(TAG, "send: Can not sent on a closed socket!")
                return
            }
            if(it.send(byteString)){
                _stateFlow.value = ++sentImageCounter
            }
        }
    }




    fun subscribeWebSocket() = binaryWebSocketController.sharedFlow
    fun subscribeCounter() = _stateFlow
}