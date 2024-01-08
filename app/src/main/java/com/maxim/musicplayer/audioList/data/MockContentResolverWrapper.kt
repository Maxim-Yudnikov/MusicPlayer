package com.maxim.musicplayer.audioList.data

import android.net.Uri

class MockContentResolverWrapper : ContentResolverWrapper {
    override fun query(sourceUri: Uri, sortOrder: String): List<AudioData> {
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
}