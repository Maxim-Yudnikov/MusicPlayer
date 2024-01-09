package com.maxim.musicplayer.player.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.musicplayer.audioList.presentation.AudioDiffUtil
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.databinding.OderAudioLayoutBinding
import com.maxim.musicplayer.databinding.OrderTitleLayoutBinding

class OrderAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<OrderAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AudioUi>()

    abstract class ItemViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: AudioUi)
    }

    class AudioViewHolder(private val binding: OderAudioLayoutBinding, private val listener: Listener) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi) {
            item.showArt(binding.artImageView, false)
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
            binding.removeFromOrderButton.setOnClickListener {
                item.removeListener(listener)
            }
        }
    }

    class TitleViewHolder(private val binding: OrderTitleLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi) {
            item.showTitle(binding.orderDescriptionTextView)
        }
    }

    override fun getItemViewType(position: Int) = if (list[position] is AudioUi.OrderTitle) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == 0) TitleViewHolder(
            OrderTitleLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        else
            AudioViewHolder(OderAudioLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), listener)

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

    interface Listener {
        fun remove(id: Long)
    }
}