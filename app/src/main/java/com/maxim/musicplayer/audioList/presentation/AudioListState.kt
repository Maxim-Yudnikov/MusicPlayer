package com.maxim.musicplayer.audioList.presentation

interface AudioListState {
    fun showList(adapter: AudioListAdapter)

    data class List(
        private val list: kotlin.collections.List<AudioUi>,
        private val playingTrackPosition: Int,
        private val updateAll: Boolean
    ) : AudioListState {
        override fun showList(adapter: AudioListAdapter) {
            if (updateAll)
                adapter.updateAll(list, playingTrackPosition)
            else
                adapter.update(list, playingTrackPosition)
        }
    }
}