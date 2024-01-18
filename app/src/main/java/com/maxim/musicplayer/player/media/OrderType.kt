package com.maxim.musicplayer.player.media

import com.maxim.musicplayer.albumList.presentation.AlbumUi

interface OrderType {
    fun same(albumUi: AlbumUi) = false

    object Empty : OrderType
    object Base : OrderType
    object Favorite : OrderType
    data class Album(private val albumUi: AlbumUi) : OrderType {
        override fun same(albumUi: AlbumUi) = this.albumUi.same(albumUi)
    }
}