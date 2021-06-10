package com.example.floapplication.util

import androidx.recyclerview.widget.DiffUtil
import com.example.floapplication.data.model.Lyric

class DiffCallBack(private val oldList: ArrayList<Lyric>, private val newList: ArrayList<Lyric>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].time === newList[newItemPosition].time
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] == newList[newItemPosition]
    }
}