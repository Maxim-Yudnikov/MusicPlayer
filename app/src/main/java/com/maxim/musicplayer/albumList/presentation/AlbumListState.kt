package com.maxim.musicplayer.albumList.presentation

interface AlbumListState {
    data class Base(private val albums: List<AlbumUi>): AlbumListState
}