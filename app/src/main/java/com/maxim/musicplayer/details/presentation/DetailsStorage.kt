package com.maxim.musicplayer.details.presentation

interface DetailsStorage {
    interface Save: com.maxim.musicplayer.core.data.Save<Long>
    interface Read: com.maxim.musicplayer.core.data.Read<Long>
    interface Mutable: Save, Read
    class Base: Mutable {
        private var cache = -1L
        override fun save(value: Long) {
            cache = value
        }

        override fun read() = cache
    }
}