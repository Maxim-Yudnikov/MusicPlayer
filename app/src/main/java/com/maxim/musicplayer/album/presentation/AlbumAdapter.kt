package com.maxim.musicplayer.album.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.maxim.musicplayer.R
import com.maxim.musicplayer.albumList.presentation.AlbumUi
import com.maxim.musicplayer.audioList.presentation.AudioListAdapter
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.databinding.AlbumTitleLayoutBinding
import com.maxim.musicplayer.databinding.AudioLayoutBinding

class AlbumAdapter(private val listener: AudioListAdapter.Listener) :
    RecyclerView.Adapter<AlbumAdapter.ItemViewHolder>() {
    private val list = mutableListOf<Any>()
    private var actualPosition = -1

    abstract class ItemViewHolder(binding: ViewBinding) : ViewHolder(binding.root) {
        open fun bind(item: AlbumUi) = Unit
        open fun bind(item: AudioUi, position: Int, actualPosition: Int) = Unit
    }

    class TitleViewHolder(private val binding: AlbumTitleLayoutBinding) :
        ItemViewHolder(binding) {
        override fun bind(item: AlbumUi) {
            item.showArt(binding.artImageView)
            item.showTitle(binding.titleTextView)
            item.showArtist(binding.artistTextView)
            item.showCount(binding.descriptionTextView)
        }
    }

    class TrackViewHolder(
        private val binding: AudioLayoutBinding,
        private val listener: AudioListAdapter.Listener,
    ) : ItemViewHolder(binding) {
        override fun bind(item: AudioUi, position: Int, actualPosition: Int) {
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
            binding.artImageView.notifyArtChanged()
            item.showArt(binding.artImageView, false)
            itemView.setOnClickListener {
                listener.open(
                    item,
                    position - 1,
                    (binding.titleTextView.context.applicationContext as ProvideMediaService).mediaService()
                )
            }
            binding.moreButton.setOnClickListener {
                listener.more(item)
            }
            val color = ContextCompat.getColor(
                binding.artImageView.context,
                if (position == actualPosition) R.color.green else R.color.black
            )
            binding.titleTextView.setTextColor(color)
        }
    }

    override fun getItemViewType(position: Int) =
        if (list[position] is AlbumUi) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        if (viewType == 0) TrackViewHolder(
            AudioLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        ) else
            TitleViewHolder(
                AlbumTitleLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (list[position] is AudioUi) holder.bind(list[position] as AudioUi, position, actualPosition)
        else holder.bind(list[position] as AlbumUi)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<Any>, actualPosition: Int) {
        list.clear()
        list.addAll(newList)
        this.actualPosition = actualPosition
        //todo diffutil
        notifyDataSetChanged()
    }
}