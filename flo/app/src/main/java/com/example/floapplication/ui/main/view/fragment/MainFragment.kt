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
                    if(binding.indicatorSeekBar.progress == binding.indicatorSeekBar.max) { // 재생이 끝나면 플레이 버튼으로 바꾸고 Status를 바꿔준다.
                        binding.btnPlay.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_play_arrow_24,
                            null
                        )
                        model.getPlayStatus(PLAYING)
                    }

                })

            // 가사 데이터 관찰에 따른 강조
            lyricsData.observe(viewLifecycleOwner,
                Observer { t ->
                    Log.d(TAG, "initViewModelis lyricsData ${t}")
                    binding.recyclerView.adapter = LyricsAdapter().apply {
                       submitList(t)
                    }
                })

            // 플레이 상태에 따라 재생 및 정지
            playStatus.observe(viewLifecycleOwner,
            Observer { ps ->
                Log.d(TAG, "initViewModel: 플레이상태는 $ps")
                if(ps == PLAYING) { // 노래가 나오고있을 때 멈춰야함.
                    model.player.value!!.pause()
                    binding.btnPlay.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_play_arrow_24,
                        null
                    )
                    MyThread().threadPause()
                }
                else { // 노래가 안나올 때 노래를 틀어야함.
                    binding.btnPlay.background =
                        resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
                    model.player.value!!.start()
                    MyThread().threadStart()
                }
            })
        }
    }

    private fun initRecyclerView(lyrics: String) {
        // 가사 시간과 가사로 자르기
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

    // Binding과 초기화
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

    // 클릭 이벤트
    private fun initListeners() {
        binding.btnPlay.setOnClickListener {
            if (!model.player.value!!.isPlaying) { // 노래가 안나올 때 노래를 틀어야함.
                model.getPlayStatus(PAUSE)
            } else { // 노래가 나올 때 노래를 멈춰야함.
                model.getPlayStatus(PLAYING)
            }
        }

    }

    // SeekBar Listener
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
                model.player.value!!.seekTo(model.songPositionData.value!! * 1000)
                Log.d(TAG, "onStopTrackingTouch: 현재 ${model.player.value!!.currentPosition}")
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
                        model.seekTo(model.player.value!!.currentPosition.toLong() / 1000) // 현재 플레이 되고 있는 시간
                        tmpIndex = findLowerBound(lyricList, (model.player.value!!.currentPosition)) // 현재 플레이 되고 있는 lyricList의 index
                        if (nowIndex != tmpIndex) { // 현재의 가사
                            lyricList[tmpIndex].highlight = true
                            if (nowIndex >= 0) {// 나머지 가사
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
