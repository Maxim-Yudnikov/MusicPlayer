package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Mutable,
    private val downBarTrackCommunication: DownBarTrackCommunication,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder
) : ViewModel(), Communication.Observe<PlayerState>, Playable {
    private var isPlaying = false

    private lateinit var cachedMediaService: MediaService

    fun init(isFirstRun: Boolean, mediaService: MediaService) {
        if (isFirstRun) {
            val track = sharedStorage.read()
            if (track != AudioUi.Empty) {
                isPlaying = true
                communication.update(
                    PlayerState.Initial(track, manageOrder.isRandom, manageOrder.isLoop)
                )
                track.start(mediaService)
                downBarTrackCommunication.setTrack(track, this)
            } else {
                communication.update(
                    PlayerState.Initial(
                        manageOrder.actualTrack(), manageOrder.isRandom, manageOrder.isLoop
                    )
                )
            }
        }
        cachedMediaService = mediaService
        mediaService.setOnCompleteListener { next() }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }

    override fun play() {
        isPlaying = !isPlaying
        if (isPlaying) {
            val track = manageOrder.actualTrack()
            track.start(cachedMediaService)
            downBarTrackCommunication.setTrack(track, this)
            communication.update(PlayerState.Running)
        } else {
            cachedMediaService.pause()
            downBarTrackCommunication.stop()
            communication.update(PlayerState.OnPause)
        }
    }

    override fun next() {
        if (manageOrder.canGoNext()) {
            isPlaying = true
            val track = manageOrder.next()
            sharedStorage.save(track)
            communication.update(
                PlayerState.Initial(
                    track,
                    manageOrder.isRandom,
                    manageOrder.isLoop
                )
            )
            track.start(cachedMediaService)
            downBarTrackCommunication.setTrack(track, this)
            cachedMediaService.setOnCompleteListener { next() }
        }
    }

    override fun previous() {
        if (cachedMediaService.currentPosition() < TIME_TO_PREVIOUS_MAKE_RESTART && manageOrder.canGoPrevious()) {
            isPlaying = true
            val track = manageOrder.previous()
            sharedStorage.save(track)
            communication.update(
                PlayerState.Initial(
                    track,
                    manageOrder.isRandom,
                    manageOrder.isLoop
                )
            )
            track.start(cachedMediaService)
            downBarTrackCommunication.setTrack(track, this)
        } else {
            communication.update(PlayerState.Running)
            val track = manageOrder.actualTrack()
            track.startAgain(cachedMediaService)
        }
        cachedMediaService.setOnCompleteListener { next() }
    }

    fun changeRandom(): Boolean {
        manageOrder.isRandom = !manageOrder.isRandom
        return manageOrder.isRandom
    }

    fun changeLoop(): Boolean {
        manageOrder.isLoop = !manageOrder.isLoop
        return manageOrder.isLoop
    }

    companion object {
        private const val TIME_TO_PREVIOUS_MAKE_RESTART = 2500
    }
}