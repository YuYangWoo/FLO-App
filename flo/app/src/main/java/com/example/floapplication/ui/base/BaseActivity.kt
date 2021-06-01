package com.example.floapplication.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.floapplication.R
import com.example.floapplication.ui.main.view.activity.MainActivity

abstract class BaseActivity<VB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    lateinit var binding: VB
    private val TAG = "BASEACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    protected open fun init() {
        initViewDataBinding()
    }

//    protected open fun splash() {
//        setTheme(R.style.Theme_FloApplication)
//    }

    protected open fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun toast(context: Context,  msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }
}