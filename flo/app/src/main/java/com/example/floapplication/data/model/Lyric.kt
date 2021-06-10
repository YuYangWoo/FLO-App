package com.example.floapplication.data.model

import java.io.Serializable
    data class Lyric(
        var time: Int,
        var lyric: String,
        var highlight: Boolean
    ): Serializable {
        constructor(): this(0,"", false)
    }

