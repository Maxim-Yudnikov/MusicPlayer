package com.maxim.musicplayer.player.presentation

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface PlayerState {
    fun show(
        artImageView: ImageView,
        titleTextView: TextView,
        artistTextView: TextView,
        playButton: ImageButton,
        seekBar: SeekBar,
        actualTimeTextView: TextView,
        durationTextView: TextView
    )

    data class Initial(private val audio: AudioUi) : PlayerState {
        @SuppressLint("SetTextI18n")
        override fun show(
            artImageView: ImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton,
            seekBar: SeekBar,
            actualTimeTextView: TextView,
            durationTextView: TextView
        ) {
            seekBar.progress = 0
            actualTimeTextView.text = "0:00"
            audio.setMaxDuration(seekBar)
            audio.showDuration(durationTextView)
            audio.showArt(artImageView)
            audio.showTitle(titleTextView)
            audio.showArtist(artistTextView)
            playButton.setImageResource(R.drawable.pause_24)
        }
    }

    object Running: PlayerState {
        override fun show(
            artImageView: ImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton,
            seekBar: SeekBar,
            actualTimeTextView: TextView,
            durationTextView: TextView
        ) {
            playButton.setImageResource(R.drawable.pause_24)
        }
    }

    object OnPause : PlayerState {
        override fun show(
            artImageView: ImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton,
            seekBar: SeekBar,
            actualTimeTextView: TextView,
            durationTextView: TextView
        ) {
            playButton.setImageResource(R.drawable.play_24)
        }
    }
}