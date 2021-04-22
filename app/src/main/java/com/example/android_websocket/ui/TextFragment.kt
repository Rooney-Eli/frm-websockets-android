package com.example.android_websocket.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android_websocket.R
import com.example.android_websocket.network.websocket.text.TextWebSocketController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TextFragment: Fragment(R.layout.fragment_text) {
    private val TAG = "AppDebug: TextFragment"
    private val viewModel: MainViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textInputET = view.findViewById<EditText>(R.id.text_input_et)
        val receivedTV = view.findViewById<TextView>(R.id.received_text_tv)
        val sendTextBtn = view.findViewById<Button>(R.id.send_text_btn)
        var textMessages = ""

        lifecycleScope.launchWhenStarted {
            viewModel.subscribeTextWebSocket().collect {
                when(it) {
                    is TextWebSocketController.TextWebSocketEvent.None -> {
                        Log.d(TAG, "WebSocketEvent: None")
                    }
                    is TextWebSocketController.TextWebSocketEvent.MessagedText -> {
                        Log.d(TAG, "WebSocketEvent: Messaged: ${it.message}")
                        textMessages += "${it.message} \n"
                        receivedTV.text = textMessages
                    }
                    is TextWebSocketController.TextWebSocketEvent.Failed -> {
                        Log.d(TAG, "WebSocketEvent: Failed: ${it.error}")
//                        reconnectBtn.visibility = View.VISIBLE
                    }
                    is TextWebSocketController.TextWebSocketEvent.Opened -> {
                        Log.d(TAG, "WebSocketEvent: Opened")
//                        reconnectBtn.visibility = View.GONE
                    }
                    is TextWebSocketController.TextWebSocketEvent.Closed -> {
                        Log.d(TAG, "WebSocketEvent: Closed")
//                        reconnectBtn.visibility = View.VISIBLE
                    }
                }
            }
        }

//        reconnectBtn.setOnClickListener {
//            viewModel.reconnect()
//        }

        sendTextBtn.setOnClickListener {
            viewModel.sendMessage(textInputET.text.toString())
            textInputET.text.clear()
        }
    }



}