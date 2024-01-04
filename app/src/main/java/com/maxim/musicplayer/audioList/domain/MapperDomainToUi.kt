package com.maxim.musicplayer.audioList.domain

import android.net.Uri
import com.maxim.musicplayer.audioList.presentation.AudioUi

class MapperDomainToUi : AudioDomain.Mapper<AudioUi> {
    override fun map(
        id: Long,
        title: String,
        artist: String,
        duration: Int,
        album: String,
        art: Uri,
        uri: Uri
    ): AudioUi = AudioUi.Base(id, title, artist, duration, album, art, uri)
}