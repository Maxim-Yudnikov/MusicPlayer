package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Read,
    private val communication: PlayerCommunication
) : ViewModel(), Communication.Observe<PlayerState>, Playable {
    private var isPlaying = true

    fun init(isFirstRun: Boolean, mediaService: MediaService) {
        if (isFirstRun) {
            isPlaying = true
            communication.update(PlayerState.Initial(sharedStorage.read()))
            sharedStorage.read().start(mediaService)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }

    override fun play(mediaService: MediaService) {
        isPlaying = !isPlaying
        if (isPlaying) {
            sharedStorage.read().start(mediaService)
            communication.update(PlayerState.Running)
        } else {
            mediaService.pause()
            communication.update(PlayerState.OnPause)
        }
    }

    override fun next() {
        TODO("Not yet implemented")
    }

    override fun previous() {
        TODO("Not yet implemented")
    }
}