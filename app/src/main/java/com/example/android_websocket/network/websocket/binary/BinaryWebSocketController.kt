package com.example.android_websocket.network.websocket.binary

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class BinaryWebSocketController : WebSocketListener() {
    private val TAG = "AppDebug: WebSocketController"

    private val _sharedFlow: MutableSharedFlow<BinaryWebSocketEvent> = MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val sharedFlow: SharedFlow<BinaryWebSocketEvent>
        get() = _sharedFlow

    override fun onOpen(webSocket: WebSocket, response: Response) {
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(BinaryWebSocketEvent.Opened)
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(BinaryWebSocketEvent.Closed)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message}")
        response?.let { Log.d(TAG, "onFailure: $it") }
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(BinaryWebSocketEvent.Failed(t.message))
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d(TAG, "onMessage: $bytes")
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(BinaryWebSocketEvent.MessagedBinary(bytes))
        }
    }

    sealed class BinaryWebSocketEvent {
        object Opened: BinaryWebSocketEvent()
        object Closed: BinaryWebSocketEvent()
        data class MessagedBinary(val message: ByteString): BinaryWebSocketEvent()
        data class Failed(val error: String?): BinaryWebSocketEvent()
        object None: BinaryWebSocketEvent()
    }

}