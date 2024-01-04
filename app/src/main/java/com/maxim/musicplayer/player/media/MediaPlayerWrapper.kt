package com.maxim.musicplayer.player.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

interface MediaPlayerWrapper : StartAudio {
    fun stop()
    class Base(private val context: Context) : MediaPlayerWrapper {
        private var mediaPlayer: MediaPlayer? = null
        private var actualUri: Uri? = null
        override fun start(uri: Uri) {
            actualUri?.let {
                if (uri == actualUri) return
                mediaPlayer?.stop()
                mediaPlayer?.release()
            }
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer!!.start()
            actualUri = uri
        }

        override fun stop() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            actualUri = null
        }
    }
}

interface StartAudio {
    fun start(uri: Uri)
}