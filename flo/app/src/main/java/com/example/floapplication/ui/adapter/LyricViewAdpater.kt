package com.example.floapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.floapplication.data.model.Lyric
import com.example.floapplication.databinding.HolderItemBinding
import com.example.floapplication.databinding.HolderLyricsBinding
import com.example.floapplication.ui.main.view.fragment.LyricBottomSheet
import com.example.floapplication.util.DiffCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LyricViewAdpater : ListAdapter<Lyric, LyricViewAdpater.LyricViewHolder>(MyDiffCallback){
    var lyricsList = ArrayList<Lyric>()
    var TAG = "LYRICS_ADAPTER"
    var kind = 0


//    fun changeList(newLyricList: ArrayList<Lyric>) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val diffCallBack = DiffCallBack(lyricsList, newLyricList) // DiffCallBack 유틸에 old, new 리스트를 넣어준다.
//            val diffResult = DiffUtil.calculateDiff(diffCallBack) // diffCallBack을 인자로 받고 결과 값으로 DiffUtil.DiffResult를 반환
//            lyricsList.apply {
//                clear()
//                addAll(newLyricList)
//            }
//            withContext(Dispatchers.Main) {
//                diffResult.dispatchUpdatesTo(LyricViewAdpater())
//            }
//        }
//
//    }

    inner class LyricViewHolder(private val binding: HolderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lyric) {
            binding.list = data
        }
    }

    object MyDiffCallback : DiffUtil.ItemCallback<Lyric>() {
        override fun areItemsTheSame(oldItem: Lyric, newItem: Lyric): Boolean {
           return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Lyric, newItem: Lyric): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricViewHolder {
        val binding = HolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LyricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}