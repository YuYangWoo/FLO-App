package com.example.floapplication.ui.main.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.floapplication.R
import com.example.floapplication.databinding.DialogLoadingBinding
import com.example.floapplication.ui.base.BaseDialog

// Loading Progress Bar
class ProgressDialog(context: Context) : BaseDialog<DialogLoadingBinding>(context, R.layout.dialog_loading) {
    override fun init() {
        super.init()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        var loadingAnimation = binding.pbLoading.drawable as AnimationDrawable
        loadingAnimation.start()
    }
}