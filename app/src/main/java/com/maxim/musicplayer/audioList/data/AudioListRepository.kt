package com.maxim.musicplayer.audioList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.cope.ContentResolverWrapper

interface AudioListRepository {
    fun data(): List<AudioDomain>

    class Base(
        private val contentResolverWrapper: ContentResolverWrapper,
        private val mapper: AudioData.Mapper<AudioDomain>
    ) : AudioListRepository {
        override fun data() = contentResolverWrapper.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
            ),
            "${MediaStore.Audio.Media.TITLE} ASC"
        ).map { it.map(mapper) }
    }
}