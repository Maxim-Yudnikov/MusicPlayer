package com.maxim.musicplayer.downBar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Navigation
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.presentation.PlayerScreen

class DownBarViewModel(
    private val trackCommunication: DownBarTrackCommunication,
    private val manageOrder: ManageOrder,
    private val communication: DownBarCommunication,
    private val navigation: Navigation.Update
): ViewModel(), Communication.Observe<DownBarState>, ReloadDownBar {

    override fun observe(owner: LifecycleOwner, observer: Observer<DownBarState>) {
        communication.observe(owner, observer)
    }

    override fun reload(track: AudioUi, isPlaying: Boolean) {
        communication.update(if (isPlaying) DownBarState.IsPlaying(track) else DownBarState.IsSopped(track))
    }

    fun init() {
        trackCommunication.init(this)
    }

    fun play() {
        trackCommunication.playButton()
    }

    fun open() {
        manageOrder.makeEmpty()
        navigation.update(PlayerScreen)
    }
}

interface ReloadDownBar {
    fun reload(track: AudioUi, isPlaying: Boolean)
}