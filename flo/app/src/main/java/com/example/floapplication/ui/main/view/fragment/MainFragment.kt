package com.example.floapplication.ui.main.view.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.media2.exoplayer.external.ExoPlayerFactory
import androidx.media2.exoplayer.external.SimpleExoPlayer
import androidx.media2.exoplayer.external.source.ProgressiveMediaSource
import androidx.media2.exoplayer.external.upstream.DefaultHttpDataSourceFactory
import com.bumptech.glide.Glide
import com.example.floapplication.R
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.databinding.FragmentMainBinding
import com.example.floapplication.ui.base.BaseFragment
import com.example.floapplication.ui.main.view.dialog.ProgressDialog
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val model: SongViewModel by sharedViewModel()
    private var songResponse = SongResponse()
    private val TAG = "MAIN_FRAGMENT"
    private var songUrl = ""
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var player: SimpleExoPlayer? = null

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

    @SuppressLint("RestrictedApi")
    private fun binding() {
        with(binding) {
            txtTitle.text = songResponse.title
            txtAlbum.text = songResponse.album
            txtSinger.text = songResponse.singer
            Glide.with(requireContext()).load(songResponse.image).into(imgAlbum)
            songUrl = songResponse.file
        }


    }


}