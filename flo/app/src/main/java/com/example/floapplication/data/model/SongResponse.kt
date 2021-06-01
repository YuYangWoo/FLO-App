package com.example.floapplication.data.model

import java.io.Serializable

data class SongResponse(
    var singer: String,
    var album: String,
    var title: String,
    var duration: Int,
    var image: String,
    var file: String,
    var lyrics: String
): Serializable {
    constructor(): this("","","",0,"","","")
}
