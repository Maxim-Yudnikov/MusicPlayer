package com.maxim.musicplayer.player.media

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.SimpleStorage

interface ManageOrder {
    fun generate(tracks: List<AudioUi>, position: Int)
    fun regenerate()
    fun next(): AudioUi
    fun previous(): AudioUi
    var isLoop: Boolean
    var isRandom: Boolean

    fun canGoNext(): Boolean
    fun canGoPrevious(): Boolean

    fun actualTrack(): AudioUi
    fun actualAbsolutePosition(): Int

    fun setActualTrack(position: Int)
    fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>)

    class Base(private val storage: SimpleStorage) : ManageOrder {
        override var isLoop = storage.read(LOOP_KEY, false)
            set(value) {
                field = value
                storage.save(LOOP_KEY, isLoop)
            }
        override var isRandom = storage.read(RANDOM_KEY, false)
            set(value) {
                field = value
                storage.save(RANDOM_KEY, isRandom)
                regenerate()
            }

        private val tracksListInNormalOrder = mutableListOf<AudioUi>()
        private val actualOrder = mutableListOf<AudioUi>()
        private var actualPosition = 0
        private var actualTrack: AudioUi = AudioUi.Empty

        override fun generate(tracks: List<AudioUi>, position: Int) {
            tracksListInNormalOrder.clear()
            tracksListInNormalOrder.addAll(tracks)
            actualOrder.clear()

            actualPosition = if (isRandom) {
                val newOrder = ArrayList(tracks.shuffled())
                val actualTrack =
                    newOrder.removeAt(newOrder.indexOf(tracksListInNormalOrder[position]))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(tracks)
                position
            }
            actualTrack = actualOrder[actualPosition]
        }

        override fun regenerate() {
            actualOrder.clear()

            actualPosition = if (isRandom) {
                val newOrder = ArrayList(tracksListInNormalOrder.shuffled())
                val actualTrack = newOrder.removeAt(newOrder.indexOf(actualTrack))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(tracksListInNormalOrder)
                tracksListInNormalOrder.indexOf(actualTrack)
            }
            actualTrack = actualOrder[actualPosition]
        }

        override fun next(): AudioUi {
            if (actualPosition != actualOrder.lastIndex) {
                actualPosition++
            } else if (isLoop) {
                actualPosition = 0
            }
            actualTrack = actualOrder[actualPosition]
            return actualTrack
        }

        override fun previous(): AudioUi {
            if (actualPosition != 0) {
                actualPosition--
            } else if (isLoop) {
                actualPosition = actualOrder.lastIndex
            }
            actualTrack = actualOrder[actualPosition]
            return actualTrack
        }

        override fun canGoNext() =
            actualPosition != actualOrder.lastIndex || isLoop

        override fun canGoPrevious() =
            actualPosition != 0 || isLoop

        override fun actualTrack() = actualTrack

        override fun actualAbsolutePosition() = tracksListInNormalOrder.indexOf(actualTrack)

        private val actualTrackPositionLiveData = MutableLiveData<Int>()

        override fun setActualTrack(position: Int) {
            actualTrackPositionLiveData.value = position
        }

        override fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>) {
            actualTrackPositionLiveData.observe(owner, observer)
        }

        companion object {
            private const val RANDOM_KEY = "RANDOM_KEY"
            private const val LOOP_KEY = "LOOP_KEY"
        }
    }
}