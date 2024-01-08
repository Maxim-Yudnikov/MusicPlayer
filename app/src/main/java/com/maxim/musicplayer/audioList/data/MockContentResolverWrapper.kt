package com.maxim.musicplayer.audioList.data

import android.net.Uri
import com.maxim.musicplayer.albumList.data.AlbumDomain

class MockContentResolverWrapper : ContentResolverWrapper {
    override fun tracks(sortOrder: String): List<AudioData> {
        val list = mutableListOf<AudioData>()
        for (i in 1..10) {
            list.add(
                AudioData(
                    i.toLong(), "Title $i", "Artist $i", i * 10000L, "Album $i",
                    Uri.EMPTY, Uri.EMPTY
                )
            )
        }
        return list
    }

    override fun albums(sortOrder: String): List<AlbumDomain> {
        TODO("Not yet implemented")
    }
}