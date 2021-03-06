package com.example.floapplication.ui.main.view.fragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floapplication.R
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.databinding.FragmentMainBinding
import com.example.floapplication.ui.adapter.LyricsAdapter
import com.example.floapplication.ui.base.BaseFragment
import com.example.floapplication.ui.main.view.dialog.ProgressDialog
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.example.floapplication.util.findLowerBound
import com.example.floapplication.util.singleton.ScrollRecyclerview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import java.lang.Runnable

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val model: SongViewModel by sharedViewModel()
    private var songResponse = SongResponse()
    private val TAG = "MAIN_FRAGMENT"
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var lyricList = ArrayList<Lyric>()
    var nowIndex = -1
    var tmpIndex = -1

    override fun init() {
        super.init()
        initViewModel()
        initListeners()
        setSeekBar()
        model.player.value = mediaPlayer
    }

    private fun initViewModel() {
        model.callSong().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    dialog.dismiss()
                    when (resource.data!!.code()) {
                        200 -> {
                            songResponse = resource.data.body()!!
                            model.getSongData(resource.data.body()!!)
                            Log.d(TAG, "initViewModel: $songResponse")
                            binding()
                            initRecyclerView(songResponse.lyrics)
                        }
                        else -> {
                            toast(requireContext(), "????????????")
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

            // Seekbar??? ?????? ???????????? Text ?????????
            songPositionTextData.observe(viewLifecycleOwner,
                Observer { t ->
                    binding.txtStart.text = t
                })

            // ???????????? ???????????? ?????? Seekbar progress ?????????
            songPositionData.observe(viewLifecycleOwner,
                Observer { t ->
                    binding.indicatorSeekBar.progress = t
                    if(binding.indicatorSeekBar.progress == binding.indicatorSeekBar.max) { // ????????? ????????? ????????? ???????????? ????????? Status??? ????????????.
                        binding.btnPlay.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_play_arrow_24,
                            null
                        )
                        model.getPlayStatus(PLAYING)
                    }

                })

            // ?????? ????????? ????????? ?????? ??????
            lyricsData.observe(viewLifecycleOwner,
                Observer { t ->
                    Log.d(TAG, "initViewModelis lyricsData ${t}")
                    binding.recyclerView.adapter = LyricsAdapter().apply {
                       submitList(t)
                    }
                })

            // ????????? ????????? ?????? ?????? ??? ??????
            playStatus.observe(viewLifecycleOwner,
            Observer { ps ->
                Log.d(TAG, "initViewModel: ?????????????????? $ps")
                if(ps == PLAYING) { // ????????? ??????????????? ??? ????????????.
                    model.player.value!!.pause()
                    binding.btnPlay.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_play_arrow_24,
                        null
                    )
                    MyThread().threadPause()
                }
                else { // ????????? ????????? ??? ????????? ????????????.
                    binding.btnPlay.background =
                        resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
                    model.player.value!!.start()
                    MyThread().threadStart()
                }
            })
        }
    }

    private fun initRecyclerView(lyrics: String) {
        // ?????? ????????? ????????? ?????????
        for (i in lyrics.split("\n")) {
            var tempLyric = Lyric()
            var tempTime = i.split("[")[1].split("]")[0].split(":") as ArrayList<String>
            tempLyric.time =
                tempTime[0].toInt() * 60000 + tempTime[1].toInt() * 1000 + tempTime[2].toInt()
            tempLyric.lyric = i.split("]")[1]
            lyricList.add(tempLyric)
        }
        model.getLyrics(lyricList)
        with(binding.recyclerView) {
            adapter = LyricsAdapter().apply {
               submitList(lyricList)
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    // Binding??? ?????????
    private fun binding() {
        binding.song = songResponse
        model.player.value!!.apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setDataSource(songResponse.file)
            prepare()
        }

    }

    // ?????? ?????????
    private fun initListeners() {
        binding.btnPlay.setOnClickListener {
            if (!model.player.value!!.isPlaying) { // ????????? ????????? ??? ????????? ????????????.
                model.getPlayStatus(PAUSE)
            } else { // ????????? ?????? ??? ????????? ????????????.
                model.getPlayStatus(PLAYING)
            }
        }

    }

    // SeekBar Listener
    private fun setSeekBar() {
        binding.indicatorSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            // ???????????? ????????? ?????????
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Log.d(TAG, "onProgressChanged: ${progress}")
                    model.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            // ???????????? ?????????
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                model.player.value!!.seekTo(model.songPositionData.value!! * 1000)
                Log.d(TAG, "onStopTrackingTouch: ?????? ${model.player.value!!.currentPosition}")
            }
        })

    }

    override fun onStop() {
        super.onStop()
        model.player.value!!.pause()
        model.getPlayStatus(PLAYING)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.player.value!!.release()
    }

    inner class MyThread : Thread() {
        var task = Runnable {
            run {
                while (model.player.value!!.isPlaying) {
                    try {
                        sleep(1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    requireActivity().runOnUiThread {
                        model.seekTo(model.player.value!!.currentPosition.toLong() / 1000) // ?????? ????????? ?????? ?????? ??????
                        tmpIndex = findLowerBound(lyricList, (model.player.value!!.currentPosition)) // ?????? ????????? ?????? ?????? lyricList??? index
                        if (nowIndex != tmpIndex) { // ????????? ??????
                            lyricList[tmpIndex].highlight = true
                            if (nowIndex >= 0) {// ????????? ??????
                                lyricList[nowIndex].highlight = false
                            }
                            nowIndex = tmpIndex
                            model.getLyrics(lyricList)
                            model.getTmpIndex(tmpIndex)
                            ScrollRecyclerview.toPosition(binding.recyclerView, tmpIndex)
                        }
                    }
                }
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

    companion object {
        const val PLAYING = 1
        const val PAUSE = 0
    }
}
