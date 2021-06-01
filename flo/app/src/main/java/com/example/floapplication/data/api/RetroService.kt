package com.example.floapplication.data.api

import com.example.floapplication.data.model.SongResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetroService {

    @GET("/2020-flo/song.json")
    suspend fun requestSong(): Response<SongResponse>

}