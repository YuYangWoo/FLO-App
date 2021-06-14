package com.example.floapplication.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.R
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderItemBinding
import com.example.floapplication.databinding.HolderLyricsBinding
import com.example.floapplication.ui.main.view.fragment.LyricBottomSheet

class LyricsAdapter : ListAdapter<Lyric, LyricsAdapter.ListViewHolder>(LyricViewAdpater.MyDiffCallback) {
    var lyricsList = ArrayList<Lyric>()
    var TAG = "LYRICS_ADAPTER"
    var kind = 0

    inner class ListViewHolder(private val binding: HolderLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }
        init {
            // 켜져있는데 켜져있으면 에러남. 어뎁터 처리해줘야할듯. 뷰홀더를 2개 만들자.
            binding.root.setOnClickListener {
                LyricBottomSheet().show((binding.root.context as AppCompatActivity).supportFragmentManager, "lyric")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderLyricsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}