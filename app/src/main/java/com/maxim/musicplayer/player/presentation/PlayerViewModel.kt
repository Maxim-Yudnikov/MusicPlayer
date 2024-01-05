package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Mutable,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder
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

    override fun next(mediaService: MediaService) {
        isPlaying = true
        val track = manageOrder.next()
        sharedStorage.save(track)
        communication.update(PlayerState.Initial(track))
        track.start(mediaService)
    }

    override fun previous(mediaService: MediaService) {
        isPlaying = true
        val track = manageOrder.previous()
        sharedStorage.save(track)
        communication.update(PlayerState.Initial(track))
        track.start(mediaService)
    }
}