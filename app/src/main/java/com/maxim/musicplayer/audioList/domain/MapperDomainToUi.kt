package com.maxim.musicplayer.audioList.domain

import android.net.Uri
import com.maxim.musicplayer.audioList.presentation.AudioUi

class MapperDomainToUi : AudioDomain.Mapper<AudioUi> {
    override fun map(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        album: String,
        artUri: Uri,
        uri: Uri
    ): AudioUi = AudioUi.Base(id, title, artist, duration, album, artUri, uri)

    override fun map(count: Int): AudioUi = AudioUi.Count(count)
    override fun map() = AudioUi.Space
}