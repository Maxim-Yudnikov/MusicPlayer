package com.maxim.musicplayer.audioList.domain

import com.maxim.musicplayer.audioList.data.AudioListRepository

interface AudioListInteractor {
    suspend fun dataWithImages(): List<AudioDomain>
    fun data(): List<AudioDomain>
    fun cachedData(): List<AudioDomain>

    class Base(private val repository: AudioListRepository): AudioListInteractor {
        private val cache = mutableListOf<AudioDomain>()
        override suspend fun dataWithImages(): List<AudioDomain> {
            val data = repository.dataWithImages()
            cache.clear()
            cache.addAll(data)
            cache.add(AudioDomain.Space)
            return cache
        }

        override fun data(): List<AudioDomain> {
            cache.clear()
            cache.addAll(repository.data())
            cache.add(AudioDomain.Space)
            return cache
        }

        override fun cachedData() = cache
    }
}