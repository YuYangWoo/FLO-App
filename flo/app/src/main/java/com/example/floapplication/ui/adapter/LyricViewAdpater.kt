package com.example.floapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderItemBinding
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LyricViewAdpater : ListAdapter<Lyric, LyricViewAdpater.LyricViewHolder>(MyDiffCallback){
    lateinit var ctx: Context

    inner class LyricViewHolder(private val binding: HolderItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }
        init {
            binding.root.setOnClickListener {
                (context as AppCompatActivity).supportFragmentManager.findFragmentByTag("lyric").let {
                    (it as BottomSheetDialogFragment).dismiss()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricViewHolder {
        val binding = HolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LyricViewHolder(binding, ctx)
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