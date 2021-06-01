package com.example.floapplication.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.floapplication.data.model.Resource
import com.example.floapplication.data.repository.SongRepository
import java.lang.Exception

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {
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
}