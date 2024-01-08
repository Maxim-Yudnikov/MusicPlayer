package com.maxim.musicplayer.album.presentation

import com.maxim.musicplayer.core.presentation.Communication

interface AlbumCommunication: Communication.Mutable<AlbumState> {
    class Base: Communication.Regular<AlbumState>(), AlbumCommunication
}