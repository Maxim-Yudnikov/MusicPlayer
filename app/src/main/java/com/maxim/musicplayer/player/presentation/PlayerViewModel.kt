package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Mutable,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder
) : ViewModel(), Communication.Observe<PlayerState>, Playable {
    private var isPlaying = true
    private var isLoop = manageOrder.isLoop
    private var isRandom = manageOrder.isRandom

    fun init(isFirstRun: Boolean, mediaService: MediaService) {
        if (isFirstRun) {
            isPlaying = true
            communication.update(
                PlayerState.Initial(
                    sharedStorage.read(),
                    isRandom,
                    isLoop
                )
            )
            sharedStorage.read().start(mediaService)
            mediaService.setOnCompleteListener {
                next(mediaService)
            }
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
        if (!(manageOrder.isLast() && !isLoop)) {
            isPlaying = true
            val track = manageOrder.next()
            sharedStorage.save(track)
            communication.update(
                PlayerState.Initial(
                    track, isRandom, isLoop
                )
            )
            track.start(mediaService)
            mediaService.setOnCompleteListener {
                next(mediaService)
            }
        }
    }

    override fun previous(mediaService: MediaService) {
        if (mediaService.currentPosition() < TIME_TO_PREVIOUS_MAKE_RESTART && !(manageOrder.isFirst() && !isLoop)) {
            isPlaying = true
            val track = manageOrder.previous()
            sharedStorage.save(track)
            communication.update(PlayerState.Initial(track, isRandom, isLoop))
            track.start(mediaService)
        } else {
            communication.update(PlayerState.Running)
            sharedStorage.read().startAgain(mediaService)
        }
        mediaService.setOnCompleteListener {
            next(mediaService)
        }
    }

    fun changeRandom(): Boolean {
        isRandom = !isRandom
        manageOrder.isRandom = isRandom
        return isRandom
    }

    fun changeLoop(): Boolean {
        isLoop = !isLoop
        manageOrder.isLoop = isLoop
        return isLoop
    }

    companion object {
        private const val TIME_TO_PREVIOUS_MAKE_RESTART = 2500
    }
}