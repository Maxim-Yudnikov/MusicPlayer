package com.maxim.musicplayer.audioList.presentation

interface AudioListState {
    fun showList(adapter: AudioListAdapter)
    data class List(private val list: kotlin.collections.List<AudioUi>): AudioListState {
        override fun showList(adapter: AudioListAdapter) {
            adapter.update(list)
        }
    }
}