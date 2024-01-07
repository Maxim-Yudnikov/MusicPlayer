package com.maxim.musicplayer.audioList.data

import android.net.Uri
import com.maxim.musicplayer.audioList.domain.AudioDomain

class MapperDataToDomain : AudioData.Mapper<AudioDomain> {
    override fun map(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        album: String,
        artUri: Uri,
        uri: Uri
    ): AudioDomain = AudioDomain.Base(id, title, artist, duration, album, artUri, uri)
}