package com.example.extrackify.utils.popups

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import com.example.extrackify.R
import kotlinx.coroutines.withContext

enum class Duration {
    LONG,
    SHORT
}

enum class ToastType {
    WARNING,
    ERROR,
    SUCCESS,
    INFO
}

fun Toast.customToast(
    activity: Activity,
    mainTitle: ToastType,
    message: String,
    iconId: Int,
    duration: Duration
) {
    val layout = activity.layoutInflater.inflate(
        R.layout.toast_layout,
        activity.findViewById(R.id.toast_container),
        false
    )
    val mainToastTitle = layout.findViewById<TextView>(R.id.toast_title)
    val toastMessage = layout.findViewById<TextView>(R.id.toast_message)
    val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)

    toastIcon.setImageResource(iconId)
    toastMessage.text = message
    mainToastTitle.text =
        when (mainTitle) {
            ToastType.ERROR -> "Something went Wrong!!"
            ToastType.INFO -> "Did you Know ?"
            ToastType.WARNING -> "Warning"
            ToastType.SUCCESS -> "Congratulation!"

        }

    var duration = when (duration) {
        Duration.LONG ->  Toast.LENGTH_LONG
        Duration.SHORT ->  Toast.LENGTH_SHORT
    }


    this.apply {
        duration = duration
        view = layout
        setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        show()
    }
}



