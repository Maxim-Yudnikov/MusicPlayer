package com.maxim.musicplayer.player.presentation

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.main.TimeTextView
import com.maxim.musicplayer.player.media.LoopState

interface PlayerState {
    fun show(
        artImageView: ArtImageView,
        titleTextView: TextView,
        artistTextView: TextView,
        playButton: ImageButton,
        randomButton: ImageButton,
        loopButton: ImageButton,
        seekBar: SeekBar,
        actualTimeTextView: TimeTextView,
        durationTextView: TimeTextView,
        favoriteImageView: ImageButton
    ) = Unit
    fun finish(goBack: GoBack) = Unit

    data class Base(
        private val audio: AudioUi,
        private val isRandom: Boolean,
        private val loopState: LoopState,
        private val onPause: Boolean,
        private val currentPosition: Int,
    ) : PlayerState {
        @SuppressLint("SetTextI18n")
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            playButton: ImageButton,
            randomButton: ImageButton,
            loopButton: ImageButton,
            seekBar: SeekBar,
            actualTimeTextView: TimeTextView,
            durationTextView: TimeTextView,
            favoriteImageView: ImageButton
        ) {
            audio.setMaxDuration(seekBar)
            seekBar.progress = currentPosition
            actualTimeTextView.showTime(currentPosition / 1000)
            audio.showDuration(durationTextView)
            audio.showArt(artImageView, true)
            audio.showTitle(titleTextView)
            audio.showArtist(artistTextView)
            audio.showFavorite(favoriteImageView)
            favoriteImageView.isEnabled = true
            playButton.setImageResource(if (onPause) R.drawable.play_24 else R.drawable.pause_24)
            val selectedColor = R.color.green
            val unselectedColor = R.color.white
            randomButton.setBackgroundColor(
                ContextCompat.getColor(
                    randomButton.context,
                    if (isRandom) selectedColor else unselectedColor
                )
            )
            loopState.show(loopButton)
        }
    }

    object Finish: PlayerState {
        override fun finish(goBack: GoBack) {
            goBack.goBack()
        }
    }
}