package com.maxim.musicplayer.downBar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.Communication

class DownBarViewModel(
    private val repository: DownBarRepository,
    private val communication: DownBarCommunication
): ViewModel(), Communication.Observe<DownBarState>, ReloadDownBar {

    override fun observe(owner: LifecycleOwner, observer: Observer<DownBarState>) {
        communication.observe(owner, observer)
    }

    override fun reload(track: AudioUi, isPlaying: Boolean) {
        communication.update(if (isPlaying) DownBarState.IsPlaying(track) else DownBarState.IsSopped(track))
    }

    fun init() {
        repository.init(this)
    }

    fun play() {
        repository.playButton()
    }
}

interface ReloadDownBar {
    fun reload(track: AudioUi, isPlaying: Boolean)
}