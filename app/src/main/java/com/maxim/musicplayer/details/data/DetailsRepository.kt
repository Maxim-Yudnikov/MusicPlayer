package com.maxim.musicplayer.details.data

import com.maxim.musicplayer.audioList.data.TracksProvider

interface DetailsRepository {
    fun data(id: Long): DetailsData

    class Base(private val tracksProvider: TracksProvider): DetailsRepository {
        override fun data(id: Long) = tracksProvider.track(id)
    }
}