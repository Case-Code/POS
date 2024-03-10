package com.casecode.pos.utils

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import coil.load
import com.casecode.pos.R
import timber.log.Timber


@BindingAdapter("isAvailable")
fun setTextWithNetworkAvailable(textView: TextView, isAvailable: Boolean) {
    textView.setBackgroundColor(
        ContextCompat.getColor(
            textView.context,
            if (isAvailable) R.color.md_theme_light_primaryContainer else R.color.md_theme_light_onSurface
        )
    )
    textView.setTextColor(
        ContextCompat.getColor(
            textView.context,
            if (isAvailable) R.color.md_theme_light_onPrimaryContainer else R.color.md_theme_light_surface
        )
    )
    val rootView = textView.rootView as? ViewGroup
    val viewGroup = rootView?.findViewById<ConstraintLayout>(R.id.cl_stepper_root)
    Timber.e("viewGroup = $viewGroup")
    if (viewGroup != null) {
        textView.slideAnimation(viewGroup)
    }
    textView.visibility = if (!isAvailable) View.VISIBLE else View.GONE

}

@BindingAdapter("imageUrl", "placeholder", "error")
fun loadImageUrl(view: ImageView, imageUrl: String?, placeholder: Drawable, error: Drawable) {
    view.load(imageUrl) {
        placeholder(placeholder)
        error(error)
    }
}

private fun TextView.slideAnimation(root: ViewGroup) {
    val transition: Transition = Slide(Gravity.BOTTOM)
    transition.setDuration(2_000L)
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(root, transition)
}
