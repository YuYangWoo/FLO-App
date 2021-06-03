package com.example.floapplication.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.repository.SongRepository
import com.example.floapplication.util.formatTimeInMillisToString
import java.lang.Exception

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {

    private val _songPositionTextData = MutableLiveData<String>() //
    val songPositionTextData: LiveData<String> = _songPositionTextData
    private val _songPositionData = MutableLiveData<Int>() //
    val songPositionData: LiveData<Int> = _songPositionData
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

    fun seekTo(position: Long) {
        _songPositionTextData.value = formatTimeInMillisToString(position)
        _songPositionData.value = position.toInt()
    }
}