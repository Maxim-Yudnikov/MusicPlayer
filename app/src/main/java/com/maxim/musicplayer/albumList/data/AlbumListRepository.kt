package com.maxim.musicplayer.albumList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.data.ContentResolverWrapper

interface AlbumListRepository {
    fun data(): List<AlbumDomain>

    class Base(private val contentResolverWrapper: ContentResolverWrapper): AlbumListRepository {

        override fun data(): List<AlbumDomain> {
            return contentResolverWrapper.albums("${MediaStore.Audio.Media.ALBUM} ASC")
        }
    }
}