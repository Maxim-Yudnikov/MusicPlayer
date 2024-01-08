package com.maxim.musicplayer.albumList.data

import com.maxim.musicplayer.audioList.domain.AudioDomain

interface AlbumDomain {
    interface Mapper<T> {
        fun map(id: Long, title: String, tracks: List<AudioDomain>): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Base(private val id: Long, private val title: String, private val tracks: List<AudioDomain>): AlbumDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(id, title, tracks)
    }
}