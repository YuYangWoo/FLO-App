package com.example.floapplication.ui.main.view.fragment

import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floapplication.R
import com.example.floapplication.databinding.LyricBottomSheetBinding
import com.example.floapplication.ui.adapter.LyricViewAdpater
import com.example.floapplication.ui.base.BaseBottomSheet
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LyricBottomSheet : BaseBottomSheet<LyricBottomSheetBinding>(R.layout.lyric_bottom_sheet) {
    private val model: SongViewModel by sharedViewModel()
    private val TAG = "LYRIC_BOTTOM_SHEET"

    override fun init() {
        super.init()
        binding.song = model.songData.value
        model.getSeekLyric(FALSE)
        Log.d(TAG, "init:${model.playStatus.value} ")
        initRecyclerView()
        initViewModel()
        initBtnListener()
        seekBar()
    }

    private fun seekBar() {
        Log.d(TAG, "seekBar: ${model.songPositionData.value}")
        binding.seekBar.max = model.songData.value!!.duration
        if (model.songPositionData.value == null) {
            binding.seekBar.progress = 0
        } else {
            binding.seekBar.progress = model.songPositionData.value!!
        }
        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            // 사용자가 바꾸고 있으면
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Log.d(TAG, "onProgressChanged: ${progress}")
                    model.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            // 시크바를 멈추면
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                model.player.value!!.seekTo(model.songPositionData.value!! * 1000)
                Log.d(TAG, "onStopTrackingTouch: 현재 ${model.player.value!!.currentPosition}")
            }
        })
    }

    private fun initViewModel() {

        model.lyricsData.observe(viewLifecycleOwner, Observer { t ->
            binding.recyclerLyric.adapter = LyricViewAdpater().apply {
                ctx = requireContext()
                songModel = model
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
                        model.player.value!!.pause()
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_play_arrow_24,
                            null
                        )

                    }
                    PAUSE -> {
                        model.player.value!!.start()
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

        // 쓰레드로 이동함에 따라 Seekbar progress 바꾸기
        model.songPositionData.observe(viewLifecycleOwner,
            Observer { t ->
                binding.seekBar.progress = t
            })

        // Seekbar에 따라 시작시간 Text 바꾸기
        model.songPositionTextData.observe(viewLifecycleOwner,
            Observer { t ->
                binding.txtStart.text = t
            })

    }

    private fun initRecyclerView() {
        with(binding.recyclerLyric) {
            adapter = LyricViewAdpater().apply {
                ctx = requireContext()
                songModel = model
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
            if (!model.player.value!!.isPlaying) { // 노래가 안나올 때 노래를 틀어야함.
                model.getPlayStatus(PAUSE)
            } else { // 노래가 나올 때 노래를 멈춰야함.
                model.getPlayStatus(PLAYING)
            }
        }

        // 가사 누르면 seek하게 하는 버튼
        binding.btnLyricSeek.setOnClickListener {
            when (model.seekLyric.value) {
                TRUE -> { // 되게 되있으니 못하게 바꿔야함.
                    model.getSeekLyric(FALSE)
                    binding.btnLyricSeek.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )

                }
                FALSE -> { // FALSE 못하게 되있으니 되게 바꿔야함.
                    model.getSeekLyric(TRUE)
                    binding.btnLyricSeek.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_200
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }

    companion object {
        const val PLAYING = 1
        const val PAUSE = 0
        const val TRUE = true
        const val FALSE = false
    }
}