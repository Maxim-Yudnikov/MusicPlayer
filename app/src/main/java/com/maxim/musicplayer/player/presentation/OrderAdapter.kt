package com.maxim.musicplayer.player.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.AudioDiffUtil
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.databinding.OderAudioLayoutBinding
import com.maxim.musicplayer.databinding.OrderTitleLayoutBinding

class OrderAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<OrderAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AudioUi>()
    private var actualPosition = -1

    abstract class ItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: AudioUi, position: Int, actualPosition: Int)
    }

    class AudioViewHolder(
        private val binding: OderAudioLayoutBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi, position: Int, actualPosition: Int) {
            item.showArt(binding.artImageView, false)
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
            binding.removeFromOrderButton.visibility = if (position == actualPosition) View.GONE else View.VISIBLE
            binding.removeFromOrderButton.setOnClickListener {
                item.removeListener(listener)
            }
            val color = ContextCompat.getColor(
                binding.artImageView.context,
                if (position < actualPosition) R.color.gray
                else if (position == actualPosition) R.color.green
                else R.color.black
            )
            binding.titleTextView.setTextColor(color)
        }
    }

    class TitleViewHolder(private val binding: OrderTitleLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi, position: Int, actualPosition: Int) {
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
            AudioViewHolder(
                OderAudioLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ), listener
            )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], position, actualPosition)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<AudioUi>, actualPosition: Int) {
        val diff = AudioDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)

        val oldPosition = this.actualPosition
        if (!(oldPosition != 1 && actualPosition == 0) && !(oldPosition == 1 && actualPosition != 2)) {
            this.actualPosition = actualPosition + 1
            notifyItemChanged(oldPosition)
            notifyItemChanged(this.actualPosition)
            result.dispatchUpdatesTo(this)
        } else {
            this.actualPosition = actualPosition + 1
            notifyDataSetChanged()
        }
    }

    interface Listener {
        fun remove(id: Long)
    }
}