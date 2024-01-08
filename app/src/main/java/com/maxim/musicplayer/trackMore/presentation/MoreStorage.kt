package com.maxim.musicplayer.trackMore.presentation

import com.maxim.musicplayer.audioList.presentation.AudioUi

interface MoreStorage {
    interface Read {
        fun read(): AudioUi
    }
    interface Save {
        fun save(value: AudioUi)
    }

    interface Mutable: Read, Save
    class Base: Mutable {
        private var cachedAudioUi: AudioUi = AudioUi.Empty
        override fun read() = cachedAudioUi

        override fun save(value: AudioUi) {
            cachedAudioUi = value
        }
    }
}