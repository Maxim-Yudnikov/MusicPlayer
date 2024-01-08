package com.maxim.musicplayer.album.presentation

import android.widget.TextView
import com.maxim.musicplayer.albumList.presentation.AlbumUi
import com.maxim.musicplayer.audioList.presentation.ArtImageView

interface AlbumState {
    fun show(
        artImageView: ArtImageView,
        titleTextView: TextView,
        artistTextView: TextView,
        countTextView: TextView
    )

    data class Base(private val albumUi: AlbumUi) : AlbumState {
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            artistTextView: TextView,
            countTextView: TextView
        ) {
            albumUi.showArt(artImageView)
            albumUi.showTitle(titleTextView)
            albumUi.showArtist(artistTextView)
            albumUi.showCount(countTextView)
        }
    }
}