package com.maxim.musicplayer.trackMore.presentation

import com.maxim.musicplayer.audioList.presentation.AudioUi

interface MoreStorage {
    interface Read {
        fun readAudio(): AudioUi
        fun fromFavorite(): Boolean
    }
    interface Save {
        fun saveAudio(value: AudioUi)
        fun saveFromFavorite(value: Boolean)
    }

    interface Mutable: Read, Save
    class Base: Mutable {
        private var cachedAudioUi: AudioUi = AudioUi.Empty
        private var fromFavorite = false

        override fun readAudio() = cachedAudioUi
        override fun fromFavorite() = fromFavorite

        override fun saveAudio(value: AudioUi) {
            cachedAudioUi = value
        }

        override fun saveFromFavorite(value: Boolean) {
            fromFavorite = value
        }
    }
}