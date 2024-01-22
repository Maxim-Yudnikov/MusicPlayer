package com.maxim.musicplayer.details.data

import android.net.Uri
import com.maxim.musicplayer.details.presentation.DetailsUi

data class DetailsData(
    private val artUri: Uri,
    private val title: String,
    private val path: String,
    private val format: String,
    private val bitrate: String,
    private val size: String,
    private val duration: Long,
    private val album: String,
    private val artist: String
) {
    fun toUi() =
        DetailsUi(artUri, title, path, format, bitrate, size, duration, album, artist)
}