package com.maxim.musicplayer.audioList.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.maxim.musicplayer.albumList.data.AlbumDomain
import com.maxim.musicplayer.audioList.domain.AudioDomain

interface ContentResolverWrapper {

    fun tracks(
        sortOrder: String
    ): List<AudioData>

    fun albums(
        sortOrder: String
    ): List<AlbumDomain>

    class Base(private val contentResolver: ContentResolver) : ContentResolverWrapper {
        private val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
        )
        private val idIndex = 0
        private val titleIndex = 1
        private val artistIndex = 2
        private val durationIndex = 3
        private val albumIndex = 4
        private val albumIdIndex = 5

        override fun tracks(sortOrder: String): List<AudioData> {
            return allTracks(sortOrder).map {
                AudioData(it.id, it.title, it.artist, it.duration, it.album, it.artUri, it.uri)
            }
        }

        override fun albums(sortOrder: String): List<AlbumDomain> {
            val tracks = allTracks(sortOrder)
            val albums = mutableMapOf<Long, ArrayList<Audio>>()
            val titles = mutableMapOf<Long, String>()
            tracks.forEach { audio ->
                albums[audio.albumId]?.add(audio) ?: {
                    albums[audio.albumId] = arrayListOf(audio)
                }
                titles[audio.albumId] = audio.album
            }

            return albums.map {
                AlbumDomain.Base(
                    it.key,
                    titles[it.key]!!,
                    it.value.map { audio ->
                        AudioDomain.Base(
                            audio.id, audio.title, audio.artist, audio.duration,
                            audio.album, audio.artUri, audio.uri
                        )
                    })
            }
        }

        private fun allTracks(sortOrder: String): List<Audio> {
            val list = mutableListOf<Audio>()
            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val duration = cursor.getLong(durationIndex)
                    if (duration < 1000) continue
                    val album = cursor.getString(albumIndex)
                    val albumId = cursor.getLong(albumIdIndex)
                    val artUri = Uri.parse("content://media/external/audio/media/$id/albumart")
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    list.add(Audio(id, title, artist, duration, album, albumId, artUri, uri))
                }
            }
            return list
        }
    }
}

private data class Audio(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String,
    val albumId: Long,
    val artUri: Uri,
    val uri: Uri
)

private data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val tracks: List<AudioData>
)