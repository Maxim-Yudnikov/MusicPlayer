package com.maxim.musicplayer.albumList.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface AlbumListCommunication: Communication.Mutable<AlbumListState> {
    class Base: Communication.Regular<AlbumListState>(), AlbumListCommunication
}