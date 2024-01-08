package com.maxim.musicplayer.albumList.presentation

import com.maxim.musicplayer.audioList.presentation.AudioUi

interface AlbumUi {
    data class Base(
        private val id: Long,
        private val title: String,
        private val tracks: List<AudioUi>
    )
}