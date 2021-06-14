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
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floapplication.R
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.LyricBottomSheetBinding
import com.example.floapplication.ui.adapter.LyricViewAdpater
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
        initViewModel()
        initBtnListener()
        binding()
    }

    private fun binding() {

    }

    private fun initViewModel() {

        model.lyricsData.observe(viewLifecycleOwner, Observer { t ->
            binding.recyclerLyric.adapter = LyricViewAdpater().apply {
                submitList(t)
            }
        })

        model.tmpIndex.observe(viewLifecycleOwner, Observer { index ->
            var centerOfScreen = binding.recyclerLyric.height / 3
            (binding.recyclerLyric.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                model.tmpIndex.value!!,
                centerOfScreen
            )
        })

        model.playStatus.observe(viewLifecycleOwner, Observer { status ->
            with(binding.btnPlay) {
                background = when (status) {
                    PLAYING -> { // 노래가 나오고 있을 때 멈춰야함.
                        Log.d(TAG, "initViewModel: ${status}")
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_play_arrow_24,
                            null
                        )
                    }
                    PAUSE -> {
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_pause_24,
                            null
                        )
                    }
                    else -> {
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_play_arrow_24,
                            null
                        )
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        with(binding.recyclerLyric) {
            adapter = LyricViewAdpater().apply {
            submitList(model.lyricsData.value!!)
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initBtnListener() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnPlay.setOnClickListener {
            if(model.playStatus.value == PLAYING) { // 음악이 재생중일 때
                model.getPlayStatus(PAUSE)
            }
            else { // 음악이 재생중이 아닐 때
                model.getPlayStatus(PLAYING)
            }
        }
    }

    companion object {
        const val PLAYING = 1
        const val PAUSE = 0
    }
}