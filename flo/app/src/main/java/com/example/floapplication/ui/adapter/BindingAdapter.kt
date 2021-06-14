package com.example.floapplication.ui.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

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
            true -> view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19.toFloat())
            else -> view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
        }
    }
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

    @BindingAdapter("format")
    @JvmStatic
    fun formatTimeInMillisToString(view: TextView, mSec: Long) {
        val hours: Long = mSec / 60 / 60 % 24
        val minutes: Long = mSec / 60 % 60
        val seconds: Long = mSec % 60
        view.text =if (mSec < 3600000) {
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }


    }
}