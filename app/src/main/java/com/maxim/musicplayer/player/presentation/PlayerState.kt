package com.maxim.musicplayer.player.presentation

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.AudioUi

interface PlayerState {
    fun show(
        artImageView: ImageView,
        titleTextView: TextView,
        artistTextView: TextView,
        playButton: ImageButton,
        randomButton: ImageButton,
        loopButton: ImageButton,
        seekBar: SeekBar,
        actualTimeTextView: TextView,
        durationTextView: TextView
    )

    data class Base(
        private val audio: AudioUi,
        private val isRandom: Boolean,
        private val isLoop: Boolean,
        private val onPause: Boolean,
        private val currentPosition: Int
    ) : PlayerState {
        @SuppressLint("SetTextI18n")
        override fun show(
            artImageView: ImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton,
            randomButton: ImageButton,
            loopButton: ImageButton,
            seekBar: SeekBar,
            actualTimeTextView: TextView,
            durationTextView: TextView
        ) {
            audio.setMaxDuration(seekBar)
            seekBar.progress = currentPosition
            actualTimeTextView.text = getTime(currentPosition / 1000)
            audio.showDuration(durationTextView)
            audio.showArt(artImageView)
            audio.showTitle(titleTextView)
            audio.showArtist(artistTextView)
            playButton.setImageResource(if (onPause) R.drawable.play_24 else R.drawable.pause_24)
            val selectedColor = R.color.green
            val unselectedColor = R.color.white
            randomButton.setBackgroundColor(
                ContextCompat.getColor(
                    randomButton.context,
                    if (isRandom) selectedColor else unselectedColor
                )
            )
            loopButton.setBackgroundColor(
                ContextCompat.getColor(
                    randomButton.context,
                    if (isLoop) selectedColor else unselectedColor
                )
            )
        }

        private fun getTime(seconds: Int): String {
            val minutes = seconds / 60
            val second = seconds % 60
            return "$minutes:${if (second < 10) "0$second" else second}"
        }
    }
}