package com.example.floapplication.ui.main.view.fragment

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.floapplication.R
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.databinding.FragmentMainBinding
import com.example.floapplication.ui.base.BaseFragment
import com.example.floapplication.ui.main.view.dialog.ProgressDialog
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.example.floapplication.util.formatTimeInMillisToString
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val model: SongViewModel by sharedViewModel()
    private var songResponse = SongResponse()
    private val TAG = "MAIN_FRAGMENT"
    private var songUrl = ""
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var simpleExoPlayer: MediaPlayer = MediaPlayer()

    override fun init() {
        super.init()
        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        model.callSong().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    dialog.dismiss()
                    when (resource.data!!.code()) {
                        200 -> {
                            songResponse = resource.data.body()!!
                            Log.d(TAG, "initViewModel: $songResponse")
                            binding()

                        }
                        else -> {
                            toast(requireContext(), "연결실패")
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    toast(
                        requireContext(),
                        resource.message + "\n" + resources.getString(R.string.connect_fail)
                    )
                    dialog.dismiss()
                }
                Resource.Status.LOADING -> {
                    dialog.show()
                }
            }
        })

        with(model) {
            songPositionTextData.observe(viewLifecycleOwner,
            Observer { t ->
                binding.txtStart.text = t
            })
        }
    }

    private fun binding() {
        with(binding) {
            txtTitle.text = songResponse.title
            txtAlbum.text = songResponse.album
            txtSinger.text = songResponse.singer
            Glide.with(requireContext()).load(songResponse.image).into(imgAlbum)
            songUrl = songResponse.file
            txtEnd.text = formatTimeInMillisToString(songResponse.duration.toLong())
            indicatorSeekBar.max = songResponse.duration
        }
        Log.d(TAG, "binding: $songUrl")
        simpleExoPlayer?.apply {
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            setDataSource(songResponse.file)
            prepare()
        }

        setSeekBar()
    }

    private fun initListeners() {
        var bool = false
        binding.btnPlay.setOnClickListener {
            if (!bool) {
                binding.btnPlay.background =
                    resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
                simpleExoPlayer.start()
                bool = true
                Thread(Runnable {
                    kotlin.run {
                        while (simpleExoPlayer.isPlaying) {
                            try {
                                Thread.sleep(1000)
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                            }
                            binding.indicatorSeekBar.progress = simpleExoPlayer.currentPosition.toInt()
                            Log.d(TAG, "initListeners: ${simpleExoPlayer.currentPosition}")

                        }
                    }

                }).start()
            } else {
                simpleExoPlayer.pause()
                binding.btnPlay.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_play_arrow_24,
                    null
                )
                bool = false
            }
        }

    }

    private fun setSeekBar() {
        binding.indicatorSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            // 사용자가 바꾸고 있으면
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) seekTo(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            // 시크바를 멈추면
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                model.songPositionData.value?.let { simpleExoPlayer!!.seekTo(it) }
            }

        })

    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        simpleExoPlayer.release()
        super.onDestroy()
    }

    fun seekTo(position: Long) {
        position?.let { nonNullPosition ->
            model.seekTo(nonNullPosition)
        }
    }

    companion object {
        private const val ACTION_START = 1
        private const val ACTION_PAUSE = 2
        private const val ACTION_STOP = 3
    }

}
