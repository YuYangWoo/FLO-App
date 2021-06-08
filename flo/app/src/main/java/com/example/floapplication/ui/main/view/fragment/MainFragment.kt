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
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.databinding.FragmentMainBinding
import com.example.floapplication.ui.adapter.LyricsAdapter
import com.example.floapplication.ui.base.BaseFragment
import com.example.floapplication.ui.main.view.dialog.ProgressDialog
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.example.floapplication.util.formatTimeInMillisToString
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import java.lang.Runnable
import java.lang.StringBuilder

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val model: SongViewModel by sharedViewModel()
    private var songResponse = SongResponse()
    private val TAG = "MAIN_FRAGMENT"
    private var songUrl = ""
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var simpleExoPlayer: MediaPlayer = MediaPlayer()
    private var lyricList = ArrayList<Lyric>()
    var nowIndex = 0

    override fun init() {
        super.init()
        initViewModel()
        initListeners()
        setSeekBar()
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
                            initRecyclerView(songResponse.lyrics)
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
            // Seekbar에 따라 시작시간 Text 바꾸기
            songPositionTextData.observe(viewLifecycleOwner,
                Observer { t ->
                    binding.txtStart.text = t
                })
            // 쓰레드로 이동함에 따라 Seekbar progress 바꾸기
            songPositionData.observe(viewLifecycleOwner,
                Observer { t ->
                    binding.indicatorSeekBar.progress = t
                })
        }
    }

    private fun initRecyclerView(lyrics: String) {
        for (i in lyrics.split("\n")) {
            var tempLyric = Lyric()
            var tempTime = i.split("[")[1].split("]")[0].split(":") as ArrayList<String>
            tempLyric.time =
                tempTime[0].toInt() * 60000 + tempTime[1].toInt() * 1000 + tempTime[2].toInt()
            tempLyric.lyric = i.split("]")[1]
            lyricList.add(tempLyric)
        }
        model.getLyrics(lyricList)
        Log.d(TAG, "initRecyclerView: ${model.lyricsData.value}")

        with(binding.recyclerView) {
            adapter = LyricsAdapter().apply {
                lyricsList = lyricList
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())

            setHasFixedSize(true)
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

        simpleExoPlayer?.apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setDataSource(songResponse.file)
            prepare()
        }

    }

    private fun initListeners() {
        binding.btnPlay.setOnClickListener {
            if (!simpleExoPlayer.isPlaying) { // 노래가 안나올 때
                binding.btnPlay.background =
                    resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
                simpleExoPlayer.start()
                MyThread().threadStart()
            } else {
                simpleExoPlayer.pause()
                binding.btnPlay.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_play_arrow_24,
                    null
                )
                MyThread().threadPause()
            }
        }

    }

    private fun setSeekBar() {
        binding.indicatorSeekBar.setOnSeekBarChangeListener(object :
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
                simpleExoPlayer.seekTo(model.songPositionData.value!! * 1000)
                Log.d(TAG, "onStopTrackingTouch: 현재 ${simpleExoPlayer.currentPosition}")
            }
        })

    }

    override fun onStop() {
        super.onStop()
        binding.btnPlay.background = resources.getDrawable(R.drawable.ic_baseline_play_arrow_24, null)
        simpleExoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    inner class MyThread : Thread() {
        var task = Runnable {
            run {
                while (simpleExoPlayer.isPlaying) {
                    try {
                        sleep(1000)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    requireActivity().runOnUiThread {
                        model.seekTo(simpleExoPlayer.currentPosition.toLong() / 1000)
                    }

                }
//                if (!simpleExoPlayer.isPlaying) {
//                    binding.btnPlay.background =
//                        resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
//                }
            }
        }

        fun threadStart() {
            var thread = Thread(task)
            thread.start()
        }

        fun threadPause() {
            var thread = Thread(task)
            thread.interrupt()
        }

    }

}
