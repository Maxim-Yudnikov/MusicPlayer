package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.player.media.Playable

interface DownBarTrackCommunication {
    fun init(reload: ReloadDownBar)
    fun setTrack(track: AudioUi, playable: Playable, isPlaying: Boolean)
    fun close()
    fun play()
    fun stop()
    fun playButton()

    class Base: DownBarTrackCommunication {
        private lateinit var cachedReload: ReloadDownBar
        private var cachedTrack: AudioUi? = null
        private var cachedPlayable: Playable? = null

        override fun init(reload: ReloadDownBar) {
            cachedReload = reload
        }

        override fun setTrack(track: AudioUi, playable: Playable, isPlaying: Boolean) {
            cachedReload.reload(track, isPlaying)
            cachedTrack = track
            cachedPlayable = playable
        }

        override fun close() {
            cachedTrack = null
            cachedReload.stop()
        }

        override fun play() {
            cachedTrack?.let {
                cachedReload.reload(it, true)
            }
        }

        override fun stop() {
            cachedTrack?.let {
                cachedReload.reload(it, false)
            }
        }

        override fun playButton() {
            cachedPlayable?.play()
        }
    }
}