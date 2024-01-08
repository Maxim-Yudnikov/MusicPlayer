package com.maxim.musicplayer.album.presentation

import com.maxim.musicplayer.albumList.presentation.AlbumUi

interface AlbumState {
    fun show(
        adapter: AlbumAdapter
    )

    data class Base(private val albumUi: AlbumUi) : AlbumState {
        override fun show(adapter: AlbumAdapter) {
            val list = mutableListOf<Any>(albumUi)
            list.addAll((albumUi as AlbumUi.Base).tracks)
            adapter.update(list, -1)
        }
    }
}