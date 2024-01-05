package com.maxim.musicplayer.player.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

interface MediaPlayerWrapper : StartAudio {
    fun pause()
    class Base(private val context: Context) : MediaPlayerWrapper {
        private var mediaPlayer: MediaPlayer? = null
        private var actualUri: Uri? = null
        override fun start(title: String, artist: String, uri: Uri, ignoreSame: Boolean) {
            actualUri?.let {
                if (uri != actualUri) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(context, uri)
                }
            }
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer!!.start()
            actualUri = uri
        }

        override fun pause() {
            mediaPlayer?.pause()
        }
    }
}