package com.maxim.musicplayer.player.media

interface Playable {
    fun play(mediaService: MediaService)
    fun next()
    fun previous()
}