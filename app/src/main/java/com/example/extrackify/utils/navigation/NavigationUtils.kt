package com.example.extrackify.utils.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

class NavigationUtils {
    companion object {

        fun navigateToActivity(context: Context, toActivity: Class<*>) {
            val intent = Intent(context, toActivity)

            context.startActivity(intent)

        }


    }
}