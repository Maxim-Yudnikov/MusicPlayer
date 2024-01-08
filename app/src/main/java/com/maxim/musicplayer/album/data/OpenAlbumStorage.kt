package com.maxim.musicplayer.album.data

import com.maxim.musicplayer.albumList.presentation.AlbumUi

interface OpenAlbumStorage {
    interface Read: com.maxim.musicplayer.core.data.Read<AlbumUi> {
        fun readActual(): AlbumUi
    }
    interface Save: com.maxim.musicplayer.core.data.Save<AlbumUi> {
        fun saveActual(value: AlbumUi)
    }
    interface Mutable: Save, Read

    class Base: Mutable {
        private var cache: AlbumUi? = null
        private var cachedActual: AlbumUi? = null

        override fun saveActual(value: AlbumUi) {
            cachedActual = value
        }

        override fun save(value: AlbumUi) {
            cache = value
        }

        override fun readActual() = cachedActual!!

        override fun read() = cache!!
    }
}