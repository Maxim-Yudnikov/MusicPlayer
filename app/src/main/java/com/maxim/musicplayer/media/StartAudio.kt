package com.maxim.musicplayer.media

import android.graphics.Bitmap
import android.net.Uri

interface StartAudio {
    fun start(title: String, artist: String, uri: Uri, icon: Bitmap?, ignoreSame: Boolean = false)
}