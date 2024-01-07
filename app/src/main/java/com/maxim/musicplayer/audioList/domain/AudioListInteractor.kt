package com.maxim.musicplayer.audioList.domain

import android.util.Log
import com.maxim.musicplayer.audioList.data.AudioListRepository
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository

interface AudioListInteractor {
    suspend fun dataWithImages(): List<AudioDomain>
    fun cachedData(): List<AudioDomain>

    class Base(
        private val repository: AudioListRepository,
        private val favoriteRepository: FavoriteListRepository
    ) : AudioListInteractor {
        private val cache = mutableListOf<AudioDomain>()
        override suspend fun dataWithImages(): List<AudioDomain> {
            val data = repository.data().toMutableList()
            val favoritesIds = favoriteRepository.singleDataIds()
            Log.d("MyLog", "favoritesIds: $favoritesIds")
            for (i in data.indices) {
                if (data[i].containsId(favoritesIds))
                    data[i] = data[i].changeFavorite()
            }
            cache.clear()
            cache.addAll(data)
            cache.add(AudioDomain.Space)
            return cache
        }

        override fun cachedData() = cache
    }
}