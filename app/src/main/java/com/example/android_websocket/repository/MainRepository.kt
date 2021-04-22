package com.example.android_websocket.repository

import android.graphics.Bitmap
import com.example.android_websocket.network.websocket.binary.BinaryWebSocketHandler
import com.example.android_websocket.network.websocket.text.TextWebSocketHandler
import com.example.android_websocket.util.generateSolidColorBitmap
import com.example.android_websocket.util.toByteString


class MainRepository(
    private val textWebSocketHandler: TextWebSocketHandler,
    private val binaryWebSocketHandler: BinaryWebSocketHandler
) {

    private val TAG = "AppDebug: MainRepository"

    fun getTextWebSocketSubscription() = textWebSocketHandler.subscribeWebSocket()
    fun getBinaryWebSocketSubscription() = binaryWebSocketHandler.subscribeWebSocket()

    fun sendText(message: String) = textWebSocketHandler.send(message)

    fun getSentTextCount() = textWebSocketHandler.subscribeCounter()
    fun getSentImageCount() = binaryWebSocketHandler.subscribeCounter()


    fun sendColor(color: Int): Bitmap {
        val bmp = generateSolidColorBitmap(100, 100, color)
        binaryWebSocketHandler.send( bmp.toByteString() )
        return bmp
    }

    fun reconnect() {
        textWebSocketHandler.reconnect()
        binaryWebSocketHandler.reconnect()
    }
}
