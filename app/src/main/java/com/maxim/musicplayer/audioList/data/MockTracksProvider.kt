package com.maxim.musicplayer.audioList.data

import android.net.Uri
import com.maxim.musicplayer.details.data.DetailsData

class MockTracksProvider : TracksProvider {

    override fun allTracks(sortOrder: String): List<Audio> {
        val list = mutableListOf<Audio>()
        for (i in 1..10) {
            list.add(
                Audio(
                    i.toLong(), "Title $i", "Artist $i", i * 10000L, "Album ${i / 5}",
                    (i / 5L), Uri.EMPTY, Uri.EMPTY
                )
            )
        }
        return list
    }

    override fun track(id: Long): DetailsData {
        TODO("Not yet implemented")
    }
}