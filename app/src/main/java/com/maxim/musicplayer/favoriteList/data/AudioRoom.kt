package com.maxim.musicplayer.favoriteList.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "favorite_tracks")
data class AudioRoom(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String,
    val artUri: Uri,
    val uri: Uri
)

class UriConverter {
    @TypeConverter
    fun toGson(uri: Uri) = uri.toString()

    @TypeConverter
    fun toUri(convertedUri: String) = Uri.parse(convertedUri)
}