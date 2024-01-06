package com.maxim.musicplayer.cope

import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.maxim.musicplayer.audioList.data.AudioData

interface ContentResolverWrapper {
    fun queryWithImages(
        sourceUri: Uri,
        sortOrder: String
    ): List<AudioData>

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


        //todo optimize get bitmap
        override fun queryWithImages(
            sourceUri: Uri,
            sortOrder: String
        ): List<AudioData> {
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
                    val albumArtUri = Uri.parse("content://media/external/audio/media/$id/albumart")
                    var parseFileDescriptor: ParcelFileDescriptor? = null
                    val bitmap = try {
                        parseFileDescriptor = contentResolver.openFileDescriptor(albumArtUri, "r")
                        val fileDescriptor = parseFileDescriptor!!.fileDescriptor
                        BitmapFactory.decodeFileDescriptor(fileDescriptor)
                    } catch (_: Exception) {
                        null
                    }
                    parseFileDescriptor?.close()
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    result.add(AudioData(id, title, artist, duration, album, bitmap, uri))
                }
            }

            return result
        }

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
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    result.add(AudioData(id, title, artist, duration, album, null, uri))
                }
            }

            return result
        }
    }
}