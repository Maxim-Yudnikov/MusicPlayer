package com.maxim.musicplayer.audioList.data

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.maxim.musicplayer.audioList.domain.AudioDomain

interface AudioListRepository {
    fun data(): List<AudioDomain>
    class Base( //todo contextResolverWrapper
        private val contentResolver: ContentResolver,
        private val mapper: AudioData.Mapper<AudioDomain>
    ) : AudioListRepository {
        override fun data(): List<AudioDomain> {
            val result = mutableListOf<AudioData>()

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
            )
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val duration = cursor.getString(durationIndex)
                    val album = cursor.getString(albumIndex)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    result.add(AudioData(id, title, artist, duration, album, uri))
                }
            }

            return result.map { it.map(mapper) }
        }
    }
}