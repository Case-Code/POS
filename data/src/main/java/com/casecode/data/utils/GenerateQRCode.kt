package com.casecode.data.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

const val WIDTH = 300
const val HEIGHT = 300

@Throws(WriterException::class)
fun String.encodeAsBitmap(): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix: BitMatrix = writer.encode(this, BarcodeFormat.QR_CODE, WIDTH, HEIGHT)

    val w: Int = bitMatrix.width
    val h: Int = bitMatrix.height
    val pixels = IntArray(w * h)
    for (y in 0 until h) {
        for (x in 0 until w) {
            pixels[y * w + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
        }
    }

    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
    return bitmap
}