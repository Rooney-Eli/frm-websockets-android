package com.example.android_websocket.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android_websocket.R
import com.example.android_websocket.network.websocket.binary.BinaryWebSocketController
import com.example.android_websocket.util.toBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BinaryFragment: Fragment(R.layout.fragment_binary) {

    private val TAG = "AppDebug: BinaryFragment"
    private val viewModel: MainViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sendRedBtn = view.findViewById<Button>(R.id.red_btn)
        val sendGreenBtn = view.findViewById<Button>(R.id.green_btn)
        val sendBlueBtn = view.findViewById<Button>(R.id.blue_btn)
        val sendBlackBtn = view.findViewById<Button>(R.id.black_btn)
        val sendWhiteBtn = view.findViewById<Button>(R.id.white_btn)

        val receivedIV = view.findViewById<ImageView>(R.id.received_iv)
        val sentIV = view.findViewById<ImageView>(R.id.sent_iv)

        lifecycleScope.launchWhenStarted {
            viewModel.subscribeBinaryWebSocket().collect {
                when(it) {
                    is BinaryWebSocketController.BinaryWebSocketEvent.None -> {
                        Log.d(TAG, "WebSocketEvent: None")
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.MessagedBinary -> {
                        Log.d(TAG, "WebSocketEvent: Messaged: ${it.message}")
                        receivedIV.setImageBitmap(it.message.toBitmap())

                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Failed -> {
                        Log.d(TAG, "WebSocketEvent: Failed: ${it.error}")
//                        reconnectBtn.visibility = View.VISIBLE
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Opened -> {
                        Log.d(TAG, "WebSocketEvent: Opened")
//                        reconnectBtn.visibility = View.GONE
                    }
                    is BinaryWebSocketController.BinaryWebSocketEvent.Closed -> {
                        Log.d(TAG, "WebSocketEvent: Closed")
//                        reconnectBtn.visibility = View.VISIBLE
                    }
                }
            }
        }

        sendRedBtn.setOnClickListener {
            sentIV.setImageBitmap(viewModel.sendColor(Color.RED))
        }

        sendGreenBtn.setOnClickListener {
            sentIV.setImageBitmap(viewModel.sendColor(Color.GREEN))
        }

        sendBlueBtn.setOnClickListener {
            sentIV.setImageBitmap(viewModel.sendColor(Color.BLUE))
        }

        sendBlackBtn.setOnClickListener {
            sentIV.setImageBitmap( viewModel.sendColor(Color.BLACK))
        }

        sendWhiteBtn.setOnClickListener {
            sentIV.setImageBitmap(viewModel.sendColor(Color.WHITE))
        }
    }

    override fun onResume() {

    }
}