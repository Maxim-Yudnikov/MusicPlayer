package com.maxim.musicplayer.player.swipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.musicplayer.audioList.presentation.AudioDiffUtil
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.databinding.PlayerArtLayoutBinding

class SwipeAdapter : RecyclerView.Adapter<SwipeAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AudioUi>()

    class ItemViewHolder(private val binding: PlayerArtLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(audioUi: AudioUi) {
            audioUi.showArt(binding.artImageView, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        PlayerArtLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<AudioUi>) {
        val diff = AudioDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}