package com.maxim.musicplayer.downBar

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface DownBarState {
    fun show(mainView: View, artImageView: ArtImageView, titleTextView: TextView, artistTextView: TextView, playButton: ImageButton)
    data class IsPlaying(private val track: AudioUi): DownBarState {
        override fun show(
            mainView: View,
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            mainView.visibility = View.VISIBLE
            track.showArt(artImageView, false)
            track.showTitle(titleTextView)
            track.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.pause_24)
        }
    }

    data class IsSopped(private val track: AudioUi): DownBarState {
        override fun show(
            mainView: View,
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            mainView.visibility = View.VISIBLE
            track.showArt(artImageView, false)
            track.showTitle(titleTextView)
            track.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.play_24)
        }
    }

    object Stop: DownBarState {
        override fun show(
            mainView: View,
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton
        ) {
            mainView.visibility = View.GONE
        }
    }
}