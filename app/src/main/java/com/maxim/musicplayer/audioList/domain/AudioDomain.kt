package com.maxim.musicplayer.audioList.domain

import android.net.Uri

interface AudioDomain {
    fun <T> map(mapper: Mapper<T>): T
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

        fun map(count: Int): T
        fun map(): T
    }

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private val uri: Uri
    ) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, title, artist, duration, album, artUri, uri)
    }

    data class Count(private val count: Int) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(count)
    }

    object Space: AudioDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }
}