package com.example.floapplication.util.singleton

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object ScrollRecyclerview {
    fun toPosition(recyclerView: RecyclerView, index: Int) {
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(index,recyclerView.height / 3 )
    }
}