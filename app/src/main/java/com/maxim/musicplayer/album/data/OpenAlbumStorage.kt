package com.maxim.musicplayer.album.data

import com.maxim.musicplayer.albumList.presentation.AlbumUi

interface OpenAlbumStorage {
    interface Read: com.maxim.musicplayer.core.data.Read<AlbumUi>
    interface Save: com.maxim.musicplayer.core.data.Save<AlbumUi>
    interface Mutable: Save, Read
    class Base: Mutable {
        private var cache: AlbumUi? = null
        override fun save(value: AlbumUi) {
            cache = value
        }

        override fun read() = cache!!
    }
}