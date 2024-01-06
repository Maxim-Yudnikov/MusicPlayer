package com.maxim.musicplayer.audioList.presentation

interface AudioListState {
    fun showList(adapter: AudioListAdapter)

    data class List(
        private val list: kotlin.collections.List<AudioUi>,
        private val playingTrackPosition: Int
    ) : AudioListState {
        override fun showList(
            adapter: AudioListAdapter,
        ) {
            adapter.update(list, playingTrackPosition)
        }
    }
}