package com.maxim.musicplayer.downBar

import android.widget.ImageButton
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface DownBarState {
    fun show(artImageView: ArtImageView, titleTextView: TextView, artistTextView: TextView, playButton: ImageButton)
    data class IsPlaying(private val track: AudioUi): DownBarState {
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            track.showArt(artImageView, false)
            track.showTitle(titleTextView)
            track.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.pause_24)
        }
    }

    data class IsSopped(private val track: AudioUi): DownBarState {
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            track.showArt(artImageView, false)
            track.showTitle(titleTextView)
            track.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.play_24)
        }
    }
}