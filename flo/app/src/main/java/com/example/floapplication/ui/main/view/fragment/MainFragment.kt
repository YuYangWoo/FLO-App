package com.example.floapplication.ui.main.view.fragment

import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.media2.exoplayer.external.SimpleExoPlayer
import com.bumptech.glide.Glide
import com.example.floapplication.R
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.databinding.FragmentMainBinding
import com.example.floapplication.ui.base.BaseFragment
import com.example.floapplication.ui.main.view.dialog.ProgressDialog
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.google.android.exoplayer2.SimpleExoPlayer.Builder
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val model: SongViewModel by sharedViewModel()
    private var songResponse = SongResponse()
    private val TAG = "MAIN_FRAGMENT"
    private var songUrl = ""
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private lateinit var simpleExoPlayer: com.google.android.exoplayer2.SimpleExoPlayer

    override fun init() {
        super.init()
        initViewModel()

    }

    private fun initViewModel() {
        model.callSong().observe(viewLifecycleOwner, Observer { resource ->
          when(resource.status) {
              Resource.Status.SUCCESS -> {
                  dialog.dismiss()
                  when(resource.data!!.code()) {
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
                  toast(requireContext(), resource.message + "\n" + resources.getString(R.string.connect_fail))
                  dialog.dismiss()
              }
              Resource.Status.LOADING -> {
                  dialog.show()
              }
            }
        })
    }

    private fun binding() {
        with(binding) {
            txtTitle.text = songResponse.title
            txtAlbum.text = songResponse.album
            txtSinger.text = songResponse.singer
            Glide.with(requireContext()).load(songResponse.image).into(imgAlbum)
            songUrl = songResponse.file
        }
        initializedPlayer()
        initListeners()
    }

    private fun initializedPlayer() {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), "mediaPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            MediaItem.fromUri(songUrl))

        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = com.google.android.exoplayer2.SimpleExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun initListeners() {
        var bool = false
        binding.btnPlay.setOnClickListener {
            if(!bool) {
                Log.d(TAG, "initListeners: 플레이버튼누름")
                bool = true
                binding.btnPlay.background = resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
                simpleExoPlayer.playWhenReady = true
                Log.d(TAG, "initListeners: 플레이버튼누름")

            }
            else {
                Log.d(TAG, "initListeners: 정지버튼누름")
                simpleExoPlayer.playWhenReady = false
                binding.btnPlay.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, null)
                bool = false
            }
        }

    }

    override fun onDestroy() {
        simpleExoPlayer.playWhenReady = false
        super.onDestroy()
    }

}