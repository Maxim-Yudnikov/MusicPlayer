package com.maxim.musicplayer.audioList.domain

import com.maxim.musicplayer.audioList.data.AudioListRepository

interface AudioListInteractor {
    fun data(): List<AudioDomain>

    class Base(private val repository: AudioListRepository): AudioListInteractor {
        override fun data(): List<AudioDomain> {
            return repository.data()
        }
    }
}