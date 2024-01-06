package com.maxim.musicplayer.downBar

import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.player.media.Playable

interface DownBarRepository {
    fun init(reload: ReloadDownBar)
    fun setTrack(track: AudioUi, playable: Playable)
    fun play()
    fun stop()
    fun playButton()

    class Base: DownBarRepository {
        private lateinit var cachedReload: ReloadDownBar
        private var cachedTrack: AudioUi? = null
        private var cachedPlayable: Playable? = null

        override fun init(reload: ReloadDownBar) {
            cachedReload = reload
        }

        override fun setTrack(track: AudioUi, playable: Playable) {
            cachedReload.reload(track, true)
            cachedTrack = track
            cachedPlayable = playable
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