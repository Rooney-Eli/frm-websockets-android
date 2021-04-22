package com.example.android_websocket.network.websocket.text

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

class TextWebSocketController : WebSocketListener() {
    private val TAG = "AppDebug: TextWebSocketController"

    private val _sharedFlow: MutableSharedFlow<TextWebSocketEvent> = MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val sharedFlow: SharedFlow<TextWebSocketEvent>
        get() = _sharedFlow

    override fun onOpen(webSocket: WebSocket, response: Response) {
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(TextWebSocketEvent.Opened)
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(TextWebSocketEvent.Closed)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message}")
        response?.let { Log.d(TAG, "onFailure: $it") }
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(TextWebSocketEvent.Failed(t.message))
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "onMessage: Received Message: $text")
        GlobalScope.launch(Dispatchers.IO) {
            _sharedFlow.tryEmit(TextWebSocketEvent.MessagedText(text))
        }
    }

    sealed class TextWebSocketEvent {
        object Opened: TextWebSocketEvent()
        object Closed: TextWebSocketEvent()
        data class MessagedText(val message: String): TextWebSocketEvent()
        data class Failed(val error: String?): TextWebSocketEvent()
        object None: TextWebSocketEvent()
    }
}