package com.maxim.musicplayer.audioList.data

import android.net.Uri

data class AudioData(
    private val id: Long,
    private val title: String,
    private val artist: String,
    private val duration: Int,
    private val album: String,
    private val art: Uri,
    private val uri: Uri
) {
    interface Mapper<T> {
        fun map(
            id: Long,
            title: String,
            artist: String,
            duration: Int,
            album: String,
            art: Uri,
            uri: Uri
        ): T
    }

    fun <T> map(mapper: Mapper<T>) = mapper.map(id, title, artist, duration, album, art, uri)
}