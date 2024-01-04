package com.maxim.musicplayer.audioList.data

import android.graphics.Bitmap
import android.net.Uri

data class AudioData(
    private val id: Long,
    private val title: String,
    private val artist: String,
    private val duration: Int,
    private val album: String,
    private val artBitmap: Bitmap?,
    private val uri: Uri
) {
    interface Mapper<T> {
        fun map(
            id: Long,
            title: String,
            artist: String,
            duration: Int,
            album: String,
            artBitmap: Bitmap?,
            uri: Uri
        ): T
    }

    fun <T> map(mapper: Mapper<T>) = mapper.map(id, title, artist, duration, album, artBitmap, uri)
}