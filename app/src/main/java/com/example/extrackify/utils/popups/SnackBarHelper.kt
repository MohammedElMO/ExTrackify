package com.example.extrackify.utils.popups

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.extrackify.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class SnackBarHelper(private val context: Context) {

    @SuppressLint("RestrictedApi")
    fun makeSnackBar(message: String, view: View, iconRes: Int?) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val layout = snack.view as SnackbarLayout
        layout.setPadding(0, 0, 0, 0)

        val snackBarLayout = LayoutInflater.from(context).inflate(R.layout.snackbar_layout,view.findViewById(R.id.snack_card), false)
        layout.addView(snackBarLayout)
        snack.show()
    }
}