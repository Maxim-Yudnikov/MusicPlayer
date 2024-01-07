package com.maxim.musicplayer.favoriteList.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.cope.presentation.Reload

//todo someday make interactor
interface FavoriteListRepository : FavoritesActions {
    fun init(reload: Reload)
    fun data(): List<AudioDomain>
    suspend fun singleDataIds(): List<Long>

    class Base(private val dao: FavoriteDao) : FavoriteListRepository {
        private lateinit var livedata: LiveData<List<AudioRoom>>
        private val data = mutableListOf<AudioDomain>()

        override fun init(reload: Reload) {
            livedata = dao.favoriteTracksLiveData()
            livedata.observeForever { list ->
                data.clear()
                data.addAll(list.map {
                    AudioDomain.Favorite(
                        it.id, it.title, it.artist, it.duration, it.album, it.artUri, it.uri
                    )
                })
                reload.reload()
            }
        }

        override fun data(): List<AudioDomain> {
            val newList = ArrayList(data)
            newList.add(0, AudioDomain.Count(data.size))
            newList.add(AudioDomain.Space)
            return newList
        }
        override suspend fun singleDataIds() =
            dao.favoriteTracks().map { it.id }

        override suspend fun addToFavorite(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            album: String,
            artUri: Uri,
            uri: Uri
        ) {
            dao.insert(AudioRoom(id, title, artist, duration, album, artUri, uri))
        }

        override suspend fun removeFromFavorites(id: Long) {
            dao.removeTrack(id)
        }
    }
}

interface FavoritesActions {
    suspend fun addToFavorite(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        album: String,
        artUri: Uri,
        uri: Uri
    )

    suspend fun removeFromFavorites(id: Long)
}