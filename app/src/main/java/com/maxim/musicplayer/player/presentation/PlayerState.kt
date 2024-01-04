package com.maxim.musicplayer.player.presentation

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface PlayerState {
    fun show(
        artImageView: ImageView,
        titleTextView: TextView,
        artistTextView: TextView,
        playButton: ImageButton
    )

    data class Running(private val audio: AudioUi) : PlayerState {
        override fun show(
            artImageView: ImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            audio.showArt(artImageView)
            audio.showTitle(titleTextView)
            audio.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.pause_24)
        }
    }
}