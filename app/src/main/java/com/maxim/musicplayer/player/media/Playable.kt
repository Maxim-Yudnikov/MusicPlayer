package com.maxim.musicplayer.player.media

interface Playable {
    fun play(mediaService: MediaService)
    fun next(mediaService: MediaService)
    fun previous(mediaService: MediaService)
}