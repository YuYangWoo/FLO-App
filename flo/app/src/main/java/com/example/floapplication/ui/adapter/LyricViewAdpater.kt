package com.example.floapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderItemBinding

class LyricViewAdpater : ListAdapter<Lyric, LyricViewAdpater.LyricViewHolder>(MyDiffCallback){

    inner class LyricViewHolder(private val binding: HolderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricViewHolder {
        val binding = HolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LyricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object MyDiffCallback : DiffUtil.ItemCallback<Lyric>() {
        override fun areItemsTheSame(oldItem: Lyric, newItem: Lyric): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Lyric, newItem: Lyric): Boolean {
            return oldItem == newItem
        }
    }

}