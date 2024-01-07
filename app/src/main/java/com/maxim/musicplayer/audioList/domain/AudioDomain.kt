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
            uri: Uri,
            isFavorite: Boolean
        ): T

        fun map(count: Int): T
        fun map(): T
    }

    fun containsId(list: List<Long>): Boolean = false
    fun changeFavorite(): AudioDomain = Space

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
            mapper.map(id, title, artist, duration, album, artUri, uri, false)

        override fun containsId(list: List<Long>) = list.contains(id)

        override fun changeFavorite() = Favorite(id, title, artist, duration, album, artUri, uri)
    }

    data class Favorite(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Long,
        private val album: String,
        private val artUri: Uri,
        private val uri: Uri
    ) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, title, artist, duration, album, artUri, uri, true)

        override fun containsId(list: List<Long>) = list.contains(id)

        override fun changeFavorite() = Base(id, title, artist, duration, album, artUri, uri)
    }

    data class Count(private val count: Int) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(count)
    }

    object Space: AudioDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }
}