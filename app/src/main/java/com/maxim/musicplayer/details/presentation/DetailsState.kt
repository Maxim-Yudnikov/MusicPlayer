package com.maxim.musicplayer.details.presentation

import android.widget.TextView
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.main.TimeTextView

interface DetailsState {
    fun show(
        artImageView: ArtImageView,
        titleTextView: TextView,
        pathTextView: TextView,
        formatTextView: TextView,
        bitrateTextView: TextView,
        sizeTextView: TextView,
        durationTextView: TimeTextView,
        albumTextView: TextView,
        artistTextView: TextView
    )

    class Base(private val details: DetailsUi): DetailsState {
        override fun show(
            artImageView: ArtImageView,
            titleTextView: TextView,
            pathTextView: TextView,
            formatTextView: TextView,
            bitrateTextView: TextView,
            sizeTextView: TextView,
            durationTextView: TimeTextView,
            albumTextView: TextView,
            artistTextView: TextView
        ) {
            details.showArt(artImageView)
            details.showTitle(titleTextView)
            details.showPath(pathTextView)
            details.showFormat(formatTextView)
            details.showBitrate(bitrateTextView)
            details.showSize(sizeTextView)
            details.showDuration(durationTextView)
            details.showAlbum(albumTextView)
            details.showArtist(artistTextView)
        }
    }
}