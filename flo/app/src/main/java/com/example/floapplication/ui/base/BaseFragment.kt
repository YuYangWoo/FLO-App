package com.example.floapplication.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment<VB : ViewDataBinding>(private val layoutId: Int) : Fragment() {
    protected lateinit var binding: VB
    private val model: SongViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        init()
        return binding.root
    }

    protected open fun init() {

    }

    protected open fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    protected open fun snackBar(msg: String) {
        var snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("확인", View.OnClickListener {
            snackbar.dismiss()
        })
        snackbar.show()
    }



}