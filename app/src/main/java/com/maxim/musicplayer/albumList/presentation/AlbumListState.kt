package com.maxim.musicplayer.albumList.presentation

interface AlbumListState {
    fun show(adapter: AlbumListAdapter)

    data class Base(private val albums: List<AlbumUi>): AlbumListState {
        override fun show(adapter: AlbumListAdapter) {
            adapter.update(albums)
        }
    }
}