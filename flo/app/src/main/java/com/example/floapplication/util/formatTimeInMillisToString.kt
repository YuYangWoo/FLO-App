package com.example.floapplication.util

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun formatTimeInMillisToString(mSec: Long): String {
    val hours: Long = mSec / 60 / 60 % 24
    val minutes: Long = mSec / 60 % 60
    val seconds: Long = mSec % 60

    return if (mSec < 3600000) {
        String.format("%02d:%02d", minutes, seconds)
    } else {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}