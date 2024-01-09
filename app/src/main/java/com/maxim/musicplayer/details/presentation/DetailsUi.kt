package com.maxim.musicplayer.details.presentation

import android.net.Uri
import android.widget.TextView
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.main.TimeTextView

data class DetailsUi(
    private val artUri: Uri,
    private val title: String,
    private val path: String,
    private val format: String,
    private val bitrate: String,
    private val samplingRate: String,
    private val size: String,
    private val duration: Long,
    private val album: String,
    private val artist: String
) {
    fun showArt(artImageView: ArtImageView) {
        artImageView.setArt(artUri, true)
    }

    fun showTitle(textView: TextView) {
        textView.text = title
    }

    fun showPath(textView: TextView) {
        textView.text = path
    }

    fun showFormat(textView: TextView) {
        textView.text = format
    }

    fun showBitrate(textView: TextView) {
        textView.text = bitrate
    }

    fun showSamplingRate(textView: TextView) {
        textView.text = samplingRate
    }

    fun showSize(textView: TextView) {
        textView.text = size
    }

    fun showDuration(timeTextView: TimeTextView) {
        timeTextView.showTime((duration / 1000).toInt())
    }

    fun showAlbum(textView: TextView) {
        textView.text = album
    }

    fun showArtist(textView: TextView) {
        textView.text = artist
    }
}