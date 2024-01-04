package com.maxim.musicplayer.audioList.domain

import android.net.Uri

interface AudioDomain {
    fun <T> map(mapper: Mapper<T>): T
    interface Mapper<T> {
        fun map(
            id: Long,
            title: String,
            artist: String,
            duration: String,
            album: String,
            uri: Uri
        ): T
    }

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: String,
        private val album: String,
        private val uri: Uri
    ) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, title, artist, duration, album, uri)
    }
}