package com.maxim.musicplayer.audioList.data

import android.net.Uri

data class AudioData(
    private val id: Long,
    private val title: String,
    private val artist: String,
    private val duration: Long,
    private val album: String,
    private val artUri: Uri,
    private val uri: Uri
) {
    interface Mapper<T> {
        fun map(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            album: String,
            artUri: Uri,
            uri: Uri
        ): T
    }

    fun <T> map(mapper: Mapper<T>) = mapper.map(id, title, artist, duration, album, artUri, uri)
}