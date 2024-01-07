package com.maxim.musicplayer.audioList.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

interface ContentResolverWrapper {

    fun query(
        sourceUri: Uri,
        sortOrder: String
    ): List<AudioData>

    class Base(private val contentResolver: ContentResolver) : ContentResolverWrapper {
        private val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
        )
        private val idIndex = 0
        private val titleIndex = 1
        private val artistIndex = 2
        private val durationIndex = 3
        private val albumIndex = 4

        override fun query(sourceUri: Uri, sortOrder: String): List<AudioData> {
            val result = mutableListOf<AudioData>()
            contentResolver.query(
                sourceUri, projection, null, null, sortOrder
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val duration = cursor.getLong(durationIndex)
                    if (duration < 1000) continue
                    val album = cursor.getString(albumIndex)
                    val artUri = Uri.parse("content://media/external/audio/media/$id/albumart")
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    result.add(AudioData(id, title, artist, duration, album, artUri, uri))
                }
            }

            return result
        }
    }
}