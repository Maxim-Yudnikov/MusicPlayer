package com.maxim.musicplayer.media

import android.media.MediaPlayer
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.maxim.musicplayer.R
import java.io.Serializable

interface LoopState : Serializable {
    fun next(): LoopState
    fun handle(mediaPlayer: MediaPlayer)
    fun valueInStorage(): Int
    fun show(imageButton: ImageButton)

    object Base : LoopState {
        override fun next() = LoopOrder
        override fun handle(mediaPlayer: MediaPlayer) {
            mediaPlayer.isLooping = false
        }

        override fun valueInStorage() = 0

        override fun show(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.loop_24)
            imageButton.setBackgroundColor(
                ContextCompat.getColor(imageButton.context, R.color.light_background)
            )
        }
    }

    object LoopOrder : LoopState {
        override fun next() = LoopTrack
        override fun handle(mediaPlayer: MediaPlayer) = Unit
        override fun valueInStorage() = 1

        override fun show(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.loop_24)
            imageButton.setBackgroundColor(
                ContextCompat.getColor(imageButton.context, R.color.green)
            )
        }
    }

    object LoopTrack : LoopState {
        override fun next() = Base
        override fun handle(mediaPlayer: MediaPlayer) {
            mediaPlayer.isLooping = true
        }

        override fun valueInStorage() = 2

        override fun show(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.loop_track_24)
            imageButton.setBackgroundColor(
                ContextCompat.getColor(imageButton.context, R.color.green)
            )
        }
    }
}