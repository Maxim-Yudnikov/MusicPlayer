package com.maxim.musicplayer.audioList.presentation

import android.widget.TextView
import com.maxim.musicplayer.R

interface AudioListState {
    fun showList(adapter: AudioListAdapter, countTextView: TextView)
    data class List(private val list: kotlin.collections.List<AudioUi>) : AudioListState {
        override fun showList(
            adapter: AudioListAdapter,
            countTextView: TextView,
        ) {
            adapter.update(list)
            countTextView.text =
                countTextView.context.getString(R.string.songs, list.size.toString())
        }
    }
}