package com.example.android_websocket.util

import android.graphics.*
import okio.ByteString
import java.io.ByteArrayOutputStream

fun generateSolidColorBitmap(height: Int, width: Int, color: Int): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val paint = Paint()
    paint.color = color
    canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
    return bmp
}

fun ByteString.toBitmap(): Bitmap {
    val ba = this.toByteArray()
    return BitmapFactory.decodeByteArray(ba, 0, ba.size)
}

fun Bitmap.toByteString(): ByteString {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray: ByteArray = stream.toByteArray()
    return ByteString.of(byteArray, 0, byteArray.size)
}



