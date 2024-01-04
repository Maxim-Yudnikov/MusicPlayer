package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Init
import com.maxim.musicplayer.player.media.MediaPlayerWrapper

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Read,
    private val communication: PlayerCommunication,
    private val mediaPlayerWrapper: MediaPlayerWrapper
) : ViewModel(), Init, Communication.Observe<PlayerState> {
    private var isPlaying = true
    fun play() {
        isPlaying = !isPlaying
        if (isPlaying) {
            sharedStorage.read().start(mediaPlayerWrapper)
            communication.update(PlayerState.Running)
        } else {
            mediaPlayerWrapper.stop()
            communication.update(PlayerState.OnPause)
        }
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(PlayerState.Initial(sharedStorage.read()))
            sharedStorage.read().start(mediaPlayerWrapper)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }
}