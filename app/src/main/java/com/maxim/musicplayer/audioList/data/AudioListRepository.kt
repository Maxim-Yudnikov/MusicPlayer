package com.maxim.musicplayer.audioList.data

import android.provider.MediaStore
import com.maxim.musicplayer.audioList.domain.AudioDomain

interface AudioListRepository {
    fun data(): List<AudioDomain>

    class Base(
        private val trackCacheDataSource: TracksCacheDataSource,
        private val mapper: AudioData.Mapper<AudioDomain>
    ) : AudioListRepository {

        override fun data(): List<AudioDomain> {
            val list: ArrayList<AudioDomain> = ArrayList(trackCacheDataSource.tracks(
                "${MediaStore.Audio.Media.TITLE} ASC"
            ).map { it.map(mapper) })
            list.add(0, AudioDomain.Count(list.size))
            return list
        }
    }
}