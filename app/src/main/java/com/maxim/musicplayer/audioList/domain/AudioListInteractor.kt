package com.maxim.musicplayer.audioList.domain

import com.maxim.musicplayer.audioList.data.AudioListRepository

interface AudioListInteractor {
    fun data(): List<AudioDomain>
    fun cachedData(): List<AudioDomain>

    class Base(private val repository: AudioListRepository): AudioListInteractor {
        private val cache = mutableListOf<AudioDomain>()
        override fun data(): List<AudioDomain> {
            cache.clear()
            cache.addAll(repository.data())
            return cache
        }

        override fun cachedData() = cache
    }
}