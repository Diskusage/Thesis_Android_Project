package com.example.sqliteapp.adapters

import android.graphics.Bitmap
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

//a custom encoder based on BarcodeEncoder library,
//needed to create appropriate backgrounds for QR codes
//so they would blend in with the rest of the background
class MyQrCodeEncoder : BarcodeEncoder() {
    fun createBitmap(matrix: BitMatrix, testResult: Boolean?): Bitmap {
        var white: Int = if (testResult == true) {
            0xDF2D1E
        } else {
            0x9BE128
        }
        if (testResult == null){
            white = 0xFAFAFA
        }
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) BLACK else white
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
    companion object {
        private const val BLACK = -0x1000000
    }
}