package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.downBar.DownBarRepository
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Mutable,
    private val downBarRepository: DownBarRepository,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder
) : ViewModel(), Communication.Observe<PlayerState>, Playable {
    private var isPlaying = true
    private var isLoop = manageOrder.isLoop
    private var isRandom = manageOrder.isRandom

    private lateinit var cachedMediaService: MediaService

    fun init(isFirstRun: Boolean, mediaService: MediaService) {
        if (isFirstRun) {
            isPlaying = true
            val track = sharedStorage.read()
            communication.update(
                PlayerState.Initial(
                    track,
                    isRandom,
                    isLoop
                )
            )
            track.start(mediaService)
            downBarRepository.setTrack(track, this)
            mediaService.setOnCompleteListener {
                next()
            }
        }
        cachedMediaService = mediaService
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }

    override fun play() {
        isPlaying = !isPlaying
        if (isPlaying) {
            val track = sharedStorage.read()
            track.start(cachedMediaService)
            downBarRepository.setTrack(track, this)
            communication.update(PlayerState.Running)
        } else {
            cachedMediaService.pause()
            downBarRepository.stop()
            communication.update(PlayerState.OnPause)
        }
    }

    override fun next() {
        if (!(manageOrder.isLast() && !isLoop)) {
            isPlaying = true
            val track = manageOrder.next()
            sharedStorage.save(track)
            communication.update(
                PlayerState.Initial(
                    track, isRandom, isLoop
                )
            )
            track.start(cachedMediaService)
            downBarRepository.setTrack(track, this)
            cachedMediaService.setOnCompleteListener {
                next()
            }
        }
    }

    override fun previous() {
        if (cachedMediaService.currentPosition() < TIME_TO_PREVIOUS_MAKE_RESTART && !(manageOrder.isFirst() && !isLoop)) {
            isPlaying = true
            val track = manageOrder.previous()
            sharedStorage.save(track)
            communication.update(PlayerState.Initial(track, isRandom, isLoop))
            track.start(cachedMediaService)
            downBarRepository.setTrack(track, this)
        } else {
            communication.update(PlayerState.Running)
            val track = sharedStorage.read()
            track.startAgain(cachedMediaService)
            downBarRepository.setTrack(track, this)
        }
        cachedMediaService.setOnCompleteListener {
            next()
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