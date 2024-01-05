package com.maxim.musicplayer.player.media

import android.net.Uri

interface StartAudio {
    fun start(title: String, artist: String, uri: Uri, ignoreSame: Boolean = false)
}