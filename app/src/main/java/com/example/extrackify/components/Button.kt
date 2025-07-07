package com.example.extrackify.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.withStyledAttributes
import com.example.extrackify.R

class Button(ctx: Context, attrs: AttributeSet? = null,
             defStyleAttr: Int = R.attr.label
) : AppCompatButton(
    ctx, attrs,defStyleAttr
) {


    init {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.button) {
                val buttonText = getString(R.styleable.button_label)
                if (buttonText != null) {
                    text = buttonText
                }
            }
        }
    }
}