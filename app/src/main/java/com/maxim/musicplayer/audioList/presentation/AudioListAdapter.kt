package com.maxim.musicplayer.audioList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.maxim.musicplayer.databinding.AudioLayoutBinding
import com.maxim.musicplayer.databinding.CountLayoutBinding
import com.maxim.musicplayer.databinding.SpaceBinding

class AudioListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<AudioListAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AudioUi>()

    abstract class ItemViewHolder(binding: ViewBinding) : ViewHolder(binding.root) {
        open fun bind(item: AudioUi, listener: Listener, position: Int) = Unit
    }

    class BaseViewHolder(private val binding: AudioLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi, listener: Listener, position: Int) {
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
            item.showArt(binding.artImageView)
            itemView.setOnClickListener {
                listener.open(item, position - 1)
            }
        }
    }

    class CountViewHolder(private val binding: CountLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi, listener: Listener, position: Int) {
            item.showTitle(binding.countTextView)
        }
    }

    class SpaceViewHolder(binding: SpaceBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int) =
        if (list[position] is AudioUi.Base) 0 else if (list[position] is AudioUi.Space) 1 else 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            0 -> BaseViewHolder(
                AudioLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            1 -> SpaceViewHolder(
                SpaceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> CountViewHolder(
                CountLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
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
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}