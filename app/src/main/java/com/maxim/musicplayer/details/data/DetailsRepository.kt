package com.maxim.musicplayer.details.data

import android.net.Uri
import com.maxim.musicplayer.audioList.data.TracksProvider

interface DetailsRepository {
    fun data(id: Long): DetailsData

    class Base(private val tracksProvider: TracksProvider) : DetailsRepository {
        override fun data(id: Long) = try {
            tracksProvider.track(id)
        } catch (e: Exception) {
            DetailsData(
                Uri.EMPTY,
                "Not found",
                "Not found",
                "",
                "",
                "",
                0,
                "Not found",
                "Not found"
            )
        }
    }
}