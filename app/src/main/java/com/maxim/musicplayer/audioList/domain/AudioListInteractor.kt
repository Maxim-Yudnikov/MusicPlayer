package com.maxim.musicplayer.audioList.domain

import com.maxim.musicplayer.audioList.data.AudioListRepository

interface AudioListInteractor {
    fun dataWithImages(): List<AudioDomain>
    fun cachedData(): List<AudioDomain>

    class Base(private val repository: AudioListRepository): AudioListInteractor {
        private val cache = mutableListOf<AudioDomain>()
        override fun dataWithImages(): List<AudioDomain> {
            val data = repository.data()
            cache.clear()
            cache.addAll(data)
            cache.add(AudioDomain.Space)
            return cache
        }

        override fun cachedData() = cache
    }
}