package com.maxim.musicplayer.audioList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxim.musicplayer.databinding.AudioLayoutBinding

class AudioListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<AudioListAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AudioUi>()

    class ItemViewHolder(private val binding: AudioLayoutBinding) : ViewHolder(binding.root) {
        fun bind(item: AudioUi, listener: Listener, position: Int) {
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
            item.showArt(binding.artImageView)
            itemView.setOnClickListener {
                listener.open(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            AudioLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], listener, position)
    }

    fun update(newList: List<AudioUi>) {
        val diff = AudioDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun open(audioUi: AudioUi, position: Int)
    }
}

class AudioDiffUtil(
    private val oldList: List<AudioUi>,
    private val newList: List<AudioUi>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}