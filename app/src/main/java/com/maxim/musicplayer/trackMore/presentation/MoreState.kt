package com.maxim.musicplayer.trackMore.presentation

import android.widget.ImageButton
import android.widget.TextView
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.main.TimeTextView

interface MoreState {
    fun show(artImageView: ArtImageView, titleTextView: TextView, descriptionTextView: TimeTextView, favoriteImageView: ImageButton)

    data class Base(private val audioUi: AudioUi): MoreState {
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            descriptionTextView: TimeTextView,
            favoriteImageView: ImageButton
        ) {
            audioUi.showArt(artImageView, true)
            audioUi.showTitle(titleTextView)
            audioUi.showDescription(descriptionTextView)
            audioUi.showFavorite(favoriteImageView)
        }
    }
}