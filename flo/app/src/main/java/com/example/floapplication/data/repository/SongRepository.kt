package com.example.floapplication.data.repository

import com.example.floapplication.data.api.RetroInstance

class SongRepository {
    suspend fun song() = RetroInstance.client.requestSong()
}