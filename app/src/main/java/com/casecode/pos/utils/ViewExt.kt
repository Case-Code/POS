package com.casecode.pos.utils

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int,
) {
    snackbarEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    }
}

fun View.showToast(toastText: String, duration: Int) {
    Toast.makeText(context, toastText, duration).show()
}

fun View.setupToast(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<Event<Int>>,
    duration: Int,
) {
    toastEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let {
            showToast(context.getString(it), duration)
        }
    }
}
