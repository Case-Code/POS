package com.casecode.pos.ui.item

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.casecode.data.utils.encodeAsBitmap
import com.google.zxing.WriterException
import java.text.DecimalFormat

@BindingAdapter("priceWithCurrency")
fun setPriceWithCurrency(textView: TextView, price: Double) {
    val formattedPrice = DecimalFormat("#,###.##").format(price) + " EGP"
    textView.text = formattedPrice
}

@BindingAdapter("textToBitmap")
fun textToBitmap(imageView: ImageView, text: String?) {
    try {
        text?.let {
            val bitmap = it.encodeAsBitmap()
            imageView.setImageBitmap(bitmap)
        }
    } catch (e: WriterException) {
        e.printStackTrace()
    }
}

@BindingAdapter("quantityText")
fun setQuantityText(textView: TextView, quantity: Double) {
    val formattedQuantity = "Qty. $quantity"
    textView.text = formattedQuantity
}