package com.maxim.musicplayer.albumList.data

import com.maxim.musicplayer.albumList.presentation.AlbumUi
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.presentation.AudioUi

class AlbumMapperDomainToUi(private val trackMapper: AudioDomain.Mapper<AudioUi>): AlbumDomain.Mapper<AlbumUi> {
    override fun map(id: Long, title: String, artist: String, tracks: List<AudioDomain>): AlbumUi =
        AlbumUi.Base(id, title, artist, tracks.map { it.map(trackMapper) })
}