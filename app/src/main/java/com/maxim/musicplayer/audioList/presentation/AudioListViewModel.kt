package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Init
import com.maxim.musicplayer.cope.Navigation
import com.maxim.musicplayer.player.presentation.OpenPlayerStorage
import com.maxim.musicplayer.player.presentation.PlayerScreen

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val sharedStorage: OpenPlayerStorage.Save,
    private val navigation: Navigation.Update
) : ViewModel(), Init, Communication.Observe<AudioListState> {
    override fun init(isFirstRun: Boolean) {
        communication.update(AudioListState.List(interactor.data().map { it.map(mapper) }))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun open(audio: AudioUi) {
        sharedStorage.save(audio)
        navigation.update(PlayerScreen)
    }
}