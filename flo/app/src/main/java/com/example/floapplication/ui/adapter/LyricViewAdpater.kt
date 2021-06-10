package com.example.floapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderItemBinding
import com.example.floapplication.databinding.HolderLyricsBinding
import com.example.floapplication.ui.main.view.fragment.LyricBottomSheet

class LyricViewAdpater : RecyclerView.Adapter<LyricViewAdpater.LyricViewHolder>(){
    var lyricsList = ArrayList<Lyric>()
    var TAG = "LYRICS_ADAPTER"
    var kind = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricViewAdpater.LyricViewHolder {
        val binding = HolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LyricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricViewAdpater.LyricViewHolder, position: Int) {
        holder.bind(lyricsList[position])
    }

    override fun getItemCount(): Int {
        return lyricsList.size
    }

    inner class LyricViewHolder(private val binding: HolderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }
    }
}