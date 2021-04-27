package com.example.sqliteapp.adapters

import android.graphics.Bitmap
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class MyQrCodeEncoder : BarcodeEncoder() {
    //    private static final int WHITE = 0xFFFFFFFF;
    fun createBitmap(matrix: BitMatrix, testResult: Boolean?): Bitmap {
        var white: Int = if (testResult == true) {
            0xDF2D1E
        } else {
            0x9BE128
        }
        if (testResult == null){
            white = 0xFFFFFFFF.toInt()
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
    } //    public Bitmap createBitmap(BitMatrix matrix, boolean testResult) {

    //        int white;
    //        if (testResult){
    //            white = 0xDF2D1E;
    //        } else {
    //            white = 0x9BE128;
    //        }
    //        int width = matrix.getWidth();
    //        int height = matrix.getHeight();
    //        int[] pixels = new int[width * height];
    //        for (int y = 0; y < height; y++) {
    //            int offset = y * width;
    //            for (int x = 0; x < width; x++) {
    //                pixels[offset + x] = matrix.get(x, y) ? BLACK : white;
    //            }
    //        }
    //        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    //        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    //        return bitmap;
    //    }
    companion object {
        private const val BLACK = -0x1000000
    }
}