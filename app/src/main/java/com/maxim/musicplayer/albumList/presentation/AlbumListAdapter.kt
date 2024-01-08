package com.maxim.musicplayer.albumList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.musicplayer.databinding.AlbumLayoutBinding

class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AlbumUi>()

    class ItemViewHolder(private val binding: AlbumLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumUi) {
            item.showArt(binding.artImageView)
            item.showTitle(binding.titleTextView)
            item.showDescription(binding.descriptionTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            AlbumLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<AlbumUi>) {
        val diff = AlbumDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class AlbumDiffUtil(
    private val oldList: List<AlbumUi>,
    private val newList: List<AlbumUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}