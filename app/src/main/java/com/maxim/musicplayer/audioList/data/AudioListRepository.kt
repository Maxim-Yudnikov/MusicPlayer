package com.maxim.musicplayer.audioList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.cope.ContentResolverWrapper

interface AudioListRepository {
    suspend fun dataWithImages(): List<AudioDomain>
    fun data(): List<AudioDomain>

    class Base(
        private val contentResolverWrapper: ContentResolverWrapper,
        private val mapper: AudioData.Mapper<AudioDomain>
    ) : AudioListRepository {
        override suspend fun dataWithImages() = contentResolverWrapper.queryWithImages(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            "${MediaStore.Audio.Media.TITLE} ASC"
        ).map { it.map(mapper) }

        override fun data(): List<AudioDomain> = contentResolverWrapper.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            "${MediaStore.Audio.Media.TITLE} ASC"
        ).map { it.map(mapper) }
    }
}