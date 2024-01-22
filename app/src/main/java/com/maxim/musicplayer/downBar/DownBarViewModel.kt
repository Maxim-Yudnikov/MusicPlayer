package com.maxim.musicplayer.downBar

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.player.presentation.PlayerScreen

class DownBarViewModel(
    private val trackCommunication: DownBarTrackCommunication,
    private val communication: DownBarCommunication,
    private val navigation: Navigation.Update
): ViewModel(), Communication.Observe<DownBarState>, ReloadDownBar {

    override fun observe(owner: LifecycleOwner, observer: Observer<DownBarState>) {
        communication.observe(owner, observer)
    }

    override fun reload(track: AudioUi, isPlaying: Boolean) {
        Log.d("MyLog", "$isPlaying")
        communication.update(if (isPlaying) DownBarState.IsPlaying(track) else DownBarState.IsSopped(track))
    }

    override fun stop() {
        communication.update(DownBarState.Stop)
    }

    fun init() {
        trackCommunication.init(this)
    }

    fun play() {
        trackCommunication.playButton()
    }

    fun open() {
        navigation.update(PlayerScreen)
    }
}

interface ReloadDownBar {
    fun reload(track: AudioUi, isPlaying: Boolean)
    fun stop()
}