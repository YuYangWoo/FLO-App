package com.example.floapplication.data.model

import java.io.Serializable

data class Lyric(
    var time: String,
    var lyric: String
): Serializable {
    constructor(): this("","")
}

