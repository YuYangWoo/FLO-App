package com.example.floapplication.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.floapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

open class BaseBottomSheet<VB: ViewDataBinding>(private val layoutId: Int) : BottomSheetDialogFragment() {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        init()
        return binding.root
    }

    protected open fun init() {

    }
    protected open fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    protected open fun snackBar(msg: String) {
        var snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("확인", View.OnClickListener {
            snackbar.dismiss()
        })
        snackbar.show()
    }
}