package com.maxim.musicplayer.player.presentation

import com.maxim.musicplayer.audioList.presentation.AudioUi

interface OpenPlayerStorage {
    interface Read {
        fun read(): AudioUi
    }

    interface Save {
        fun save(value: AudioUi)
    }

    interface Mutable: Read, Save
    class Base: Mutable {
        private var cache: AudioUi? = null
        override fun read() = cache!!

        override fun save(value: AudioUi) {
            cache = value
        }
    }
}