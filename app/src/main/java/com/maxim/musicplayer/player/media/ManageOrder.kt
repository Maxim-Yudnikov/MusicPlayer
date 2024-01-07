package com.maxim.musicplayer.player.media

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.data.SimpleStorage

interface ManageOrder {
    fun generate(tracks: List<AudioUi.Abstract>, position: Int)
    fun regenerate()
    fun next(): AudioUi
    fun previous(): AudioUi
    fun initLoop(mediaPlayer: MediaPlayer)
    fun changeLoop(mediaPlayer: MediaPlayer)
    fun loopState(): LoopState
    var isRandom: Boolean

    fun canGoNext(): Boolean
    fun canGoPrevious(): Boolean

    fun actualTrack(): AudioUi
    fun actualAbsolutePosition(): Int

    fun setActualTrack(position: Int)
    fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>)

    fun changeActualFavorite()

    class Base(private val storage: SimpleStorage) : ManageOrder {
        private var loopState: LoopState = storage.read(LOOP_KEY, LoopState.Base)
        override var isRandom = storage.read(RANDOM_KEY, false)
            set(value) {
                field = value
                storage.save(RANDOM_KEY, isRandom)
                regenerate()
            }

        private val defaultOrder = mutableListOf<Long>()
        private val trackMap = mutableMapOf<Long, AudioUi>()
        private val actualOrder = mutableListOf<Long>()
        private var actualPosition = 0
        private val actualTrackPositionLiveData = MutableLiveData<Int>()

        override fun generate(tracks: List<AudioUi.Abstract>, position: Int) {
            defaultOrder.clear()
            defaultOrder.addAll(tracks.map { it.id })
            trackMap.clear()
            tracks.forEach {
                trackMap[it.id] = it
            }
            actualOrder.clear()

            actualPosition = if (isRandom) {
                val newOrder = ArrayList(defaultOrder.shuffled())
                val actualTrack =
                    newOrder.removeAt(newOrder.indexOf(defaultOrder[position]))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(defaultOrder)
                position
            }
        }

        override fun regenerate() {
            actualOrder.clear()

            actualPosition = if (isRandom) {
                val newOrder = ArrayList(defaultOrder.shuffled())
                val actualTrack = newOrder.removeAt(newOrder.indexOf(actualOrder[actualPosition]))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(defaultOrder)
                defaultOrder.indexOf(actualOrder[actualPosition])
            }
        }

        override fun next(): AudioUi {
            if (actualPosition != actualOrder.lastIndex) {
                actualPosition++
            } else if (loopState == LoopState.LoopOrder) {
                actualPosition = 0
            }
            return trackMap[actualOrder[actualPosition]]!!
        }

        override fun previous(): AudioUi {
            if (actualPosition != 0) {
                actualPosition--
            } else if (loopState == LoopState.LoopOrder) {
                actualPosition = actualOrder.lastIndex
            }
            return trackMap[actualOrder[actualPosition]]!!
        }

        override fun canGoNext() =
            actualPosition != actualOrder.lastIndex || loopState == LoopState.LoopOrder

        override fun canGoPrevious() =
            actualPosition != 0 || loopState == LoopState.LoopOrder

        override fun actualTrack() = trackMap[actualOrder[actualPosition]]!!

        override fun actualAbsolutePosition() = defaultOrder.indexOf(actualOrder[actualPosition])

        override fun setActualTrack(position: Int) {
            actualTrackPositionLiveData.value = position
        }

        override fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>) {
            actualTrackPositionLiveData.observe(owner, observer)
        }

        override fun initLoop(mediaPlayer: MediaPlayer) {
            loopState.handle(mediaPlayer)
        }

        override fun changeLoop(mediaPlayer: MediaPlayer) {
            loopState = loopState.next()
            storage.save(LOOP_KEY, loopState)
            loopState.handle(mediaPlayer)
        }

        override fun changeActualFavorite() {
            trackMap[actualOrder[actualPosition]] = trackMap[actualOrder[actualPosition]]!!.changeFavorite()
        }

        override fun loopState() = loopState

        companion object {
            private const val RANDOM_KEY = "RANDOM_KEY"
            private const val LOOP_KEY = "LOOP_KEY"
        }
    }
}