package com.maxim.musicplayer.audioList.domain

import android.graphics.Bitmap
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
            artBitmap: Bitmap?,
            uri: Uri
        ): T

        fun map(count: Int): T
    }

    data class Base(
        private val id: Long,
        private val title: String,
        private val artist: String,
        private val duration: Int,
        private val album: String,
        private val artBitmap: Bitmap?,
        private val uri: Uri
    ) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, title, artist, duration, album, artBitmap, uri)
    }

    data class Count(private val count: Int) : AudioDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(count)
    }
}