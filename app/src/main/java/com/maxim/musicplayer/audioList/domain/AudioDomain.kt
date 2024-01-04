package com.maxim.musicplayer.audioList.domain

import android.net.Uri

interface AudioDomain {
    fun <T> map(mapper: Mapper<T>): T
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

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Int,
        private val album: String,
        private val art: Uri,
        private val uri: Uri
    ) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, title, artist, duration, album, art, uri)
    }
}