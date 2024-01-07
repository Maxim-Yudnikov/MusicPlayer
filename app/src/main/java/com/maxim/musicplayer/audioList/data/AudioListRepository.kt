package com.maxim.musicplayer.audioList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.domain.AudioDomain

interface AudioListRepository {
    fun data(): List<AudioDomain>

    class Base(
        private val contentResolverWrapper: ContentResolverWrapper,
        private val mapper: AudioData.Mapper<AudioDomain>
    ) : AudioListRepository {

        override fun data(): List<AudioDomain> {
            val list: ArrayList<AudioDomain> = ArrayList(contentResolverWrapper.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                "${MediaStore.Audio.Media.TITLE} ASC"
            ).map { it.map(mapper) })
            list.add(0, AudioDomain.Count(list.size))
            return list
        }
    }
}