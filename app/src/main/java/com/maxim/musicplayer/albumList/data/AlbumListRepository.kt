package com.maxim.musicplayer.albumList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.data.TracksCacheDataSource

interface AlbumListRepository {
    fun data(): List<AlbumDomain>

    class Base(private val trackCacheDataSource: TracksCacheDataSource): AlbumListRepository {

        override fun data(): List<AlbumDomain> {
            return trackCacheDataSource.albums("${MediaStore.Audio.Media.ALBUM} ASC")
        }
    }
}