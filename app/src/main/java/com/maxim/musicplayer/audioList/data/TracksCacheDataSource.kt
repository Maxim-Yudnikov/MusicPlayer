package com.maxim.musicplayer.audioList.data

import android.net.Uri
import com.maxim.musicplayer.albumList.data.AlbumDomain
import com.maxim.musicplayer.audioList.domain.AudioDomain

interface TracksCacheDataSource {

    fun tracks(
        sortOrder: String
    ): List<AudioData>

    fun albums(
        sortOrder: String
    ): List<AlbumDomain>

    class Base(private val tracksProvider: TracksProvider) : TracksCacheDataSource {

        override fun tracks(sortOrder: String): List<AudioData> {
            return tracksProvider.allTracks(sortOrder).map {
                AudioData(it.id, it.title, it.artist, it.duration, it.album, it.artUri, it.uri)
            }
        }

        override fun albums(sortOrder: String): List<AlbumDomain> {
            val tracks = tracksProvider.allTracks(sortOrder)
            val albums = mutableMapOf<Long, ArrayList<Audio>>()
            val titles = mutableMapOf<Long, String>()
            val artists = mutableMapOf<Long, String>()
            tracks.forEach { audio ->
                if (albums[audio.albumId] == null) {
                    albums[audio.albumId] = arrayListOf(audio)
                    titles[audio.albumId] = audio.album
                    artists[audio.albumId] = audio.artist
                }
                else {
                    albums[audio.albumId]!!.add(audio)
                }
            }

            albums.forEach {
                it.value.sortBy { track -> track.title }
            }

            return albums.map {
                AlbumDomain.Base(
                    it.key,
                    titles[it.key]!!,
                    artists[it.key]!!,
                    it.value.map { audio ->
                        AudioDomain.Base(
                            audio.id, audio.title, audio.artist, audio.duration,
                            audio.album, audio.artUri, audio.uri
                        )
                    })
            }
        }
    }
}

data class Audio(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String,
    val albumId: Long,
    val artUri: Uri,
    val uri: Uri
)