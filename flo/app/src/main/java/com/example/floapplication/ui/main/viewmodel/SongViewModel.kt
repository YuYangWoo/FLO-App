package com.example.floapplication.ui.main.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.model.SongResponse
import com.example.floapplication.data.repository.SongRepository
import com.example.floapplication.util.formatTimeInMillisToString
import java.lang.Exception

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {

    private val _songData = MutableLiveData<SongResponse>()
    val songData: LiveData<SongResponse> = _songData
    private val _songPositionTextData = MutableLiveData<String>() //
    val songPositionTextData: LiveData<String> = _songPositionTextData
    private val _songPositionData = MutableLiveData<Int>() //
    val songPositionData: LiveData<Int> = _songPositionData
    private val _lyricsData = MutableLiveData<ArrayList<Lyric>>()
    val lyricsData: LiveData<ArrayList<Lyric>> = _lyricsData
    private val _playStatus = MutableLiveData<Int>()
    val playStatus = _playStatus
    private val _tmPIndex = MutableLiveData<Int>()
    val tmpIndex: LiveData<Int> = _tmPIndex
    val player = MutableLiveData<MediaPlayer>()
    private val _seekLyric = MutableLiveData<Boolean>()
    val seekLyric: LiveData<Boolean> = _seekLyric

    // Song 통신
    fun callSong() = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(songRepository.song()))
        }
        catch(e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        }
    }

    fun getSongData(songResponse: SongResponse) {
        _songData.value = songResponse
    }

    fun getLyrics(lyric: ArrayList<Lyric>) {
        _lyricsData.value = lyric
    }

    fun seekTo(position: Long) {
        _songPositionTextData.value = formatTimeInMillisToString(position)
        _songPositionData.value = position.toInt()
    }

    fun getPlayStatus(status: Int) {
        _playStatus.value = status
    }

    fun getTmpIndex(index: Int) {
        _tmPIndex.value = index
    }

    fun getSeekLyric(bool: Boolean) {
        _seekLyric.value = bool
    }
}