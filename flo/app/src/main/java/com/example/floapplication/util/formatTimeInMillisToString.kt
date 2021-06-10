package com.example.floapplication.util

import android.util.Log
import com.example.floapplication.data.model.Lyric

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

fun findLowerBound(lyrics: List<Lyric>, currentTime: Int): Int {
    var start = 0
    var mid = 0
    var end = lyrics.size - 1

    if (currentTime >= lyrics[end].time) { // 현재노래시간이 가사 시간보다크면
        return end
    }

    while (end > start) { // end가 start보다 같거나 작아지면 종료
        mid = (start + end) / 2
        if (lyrics[mid].time >= currentTime) {
            end = mid
        } else {
            start = mid + 1
        }
    }

    return if (end == 0) 0 else end - 1

}


