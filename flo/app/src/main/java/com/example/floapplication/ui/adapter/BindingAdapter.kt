package com.example.floapplication.ui.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @BindingAdapter("highLightBold")
    @JvmStatic
    fun highLightBold(view: TextView, isHighLight : Boolean) {
        when (isHighLight) {
            true -> view.setTypeface(null, Typeface.BOLD)
            else -> view.setTypeface(null, Typeface.NORMAL)
        }
    }

    @BindingAdapter("highLightSize")
    @JvmStatic
    fun highLightSize(view: TextView, isHighLight : Boolean) {
        when (isHighLight) {
            true -> view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.toFloat())
            else -> view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.toFloat())
        }
    }
}