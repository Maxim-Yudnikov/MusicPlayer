package com.maxim.musicplayer.audioList.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.maxim.musicplayer.details.data.DetailsData

interface TracksProvider {
    fun allTracks(sortOrder: String): List<Audio>
    fun track(id: Long): DetailsData

    class Base(private val contentResolver: ContentResolver) : TracksProvider {

        override fun allTracks(sortOrder: String): List<Audio> {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
            )

            val idIndex = 0
            val titleIndex = 1
            val artistIndex = 2
            val durationIndex = 3
            val albumIndex = 4
            val albumIdIndex = 5

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

        override fun track(id: Long): DetailsData {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Images.Media.DATA,
                MediaStore.Audio.Media.BITRATE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
            )

            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, ""
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    if (cursor.getLong(0) != id) continue

                    val artUri = Uri.parse("content://media/external/audio/media/$id/albumart")
                    val title = cursor.getString(1)
                    val path = cursor.getString(2)
                    val bitrate = cursor.getString(3)
                    val size = cursor.getString(4)
                    val duration = cursor.getLong(5)
                    val album = cursor.getString(6)
                    val artist = cursor.getString(7)


                    return DetailsData(
                        artUri,
                        title,
                        path,
                        path.split(".").last(),
                        bitrate,
                        "${size.toInt() / 1024} KB",
                        duration,
                        album,
                        artist
                    )
                }
            }

            throw IllegalStateException("track not found")
        }
    }
}