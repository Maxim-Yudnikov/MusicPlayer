package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val downBarTrackCommunication: DownBarTrackCommunication,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService
) : ViewModel(), Communication.Observe<PlayerState>, Playable {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val track = manageOrder.actualTrack()
            if (track != AudioUi.Empty) {
                communication.update(
                    PlayerState.Initial(track, manageOrder.isRandom, manageOrder.isLoop, false)
                )
                track.start(mediaServiceProvider.mediaService())
                downBarTrackCommunication.setTrack(track, this)
            } else {
                manageOrder.syncActualTrack()
                communication.update(
                    PlayerState.Initial(
                        manageOrder.actualTrack(),
                        manageOrder.isRandom,
                        manageOrder.isLoop,
                        !mediaServiceProvider.mediaService().isPlaying()
                    )
                )
            }
        }
    }

    override fun play() {
        mediaServiceProvider.mediaService().play()
    }

    override fun next() {
        mediaServiceProvider.mediaService().next()
    }

    override fun previous() {
        mediaServiceProvider.mediaService().previous()
    }

    fun changeRandom(): Boolean {
        manageOrder.isRandom = !manageOrder.isRandom
        return manageOrder.isRandom
    }

    fun changeLoop(): Boolean {
        manageOrder.isLoop = !manageOrder.isLoop
        return manageOrder.isLoop
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }
}