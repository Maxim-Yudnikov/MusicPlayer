package com.maxim.musicplayer.audioList.data

import android.graphics.Bitmap
import android.net.Uri
import com.maxim.musicplayer.audioList.domain.AudioDomain

class MapperDataToDomain : AudioData.Mapper<AudioDomain> {
    override fun map(
        id: Long,
        title: String,
        artist: String,
        duration: Int,
        album: String,
        artBitmap: Bitmap?,
        uri: Uri
    ): AudioDomain = AudioDomain.Base(id, title, artist, duration, album, artBitmap, uri)
}