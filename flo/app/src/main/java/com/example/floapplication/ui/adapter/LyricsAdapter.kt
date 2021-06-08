package com.example.floapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.R
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderLyricsBinding

class LyricsAdapter : RecyclerView.Adapter<LyricsAdapter.ListViewHolder>() {
    var lyricsList = ArrayList<Lyric>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsAdapter.ListViewHolder {
        val binding = HolderLyricsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricsAdapter.ListViewHolder, position: Int) {
        holder.bind(lyricsList[position])
    }

    override fun getItemCount(): Int {
        return lyricsList.size
    }

    inner class ListViewHolder(private val binding: HolderLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }

    }
}