package com.example.floapplication.ui.main.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floapplication.R
import com.example.floapplication.databinding.LyricBottomSheetBinding
import com.example.floapplication.ui.adapter.LyricsAdapter
import com.example.floapplication.ui.base.BaseBottomSheet
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LyricBottomSheet : BaseBottomSheet<LyricBottomSheetBinding>(R.layout.lyric_bottom_sheet) {
    private val model: SongViewModel by sharedViewModel()
    private val TAG = "LYRIC_BOTTOM_SHEET"
    override fun init() {
        super.init()
        binding.song = model.songData.value
        Log.d(TAG, "init:${model.lyricsData} ")
        initRecyclerView()
    }
    private fun initRecyclerView() {
        with(binding.recyclerLyric) {
            adapter = LyricsAdapter().apply {
                lyricsList = model.lyricsData.value!!
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

}