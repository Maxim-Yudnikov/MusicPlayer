package com.maxim.musicplayer.player.media

import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.SimpleStorage

interface ManageOrder {
    fun generate(tracks: List<AudioUi>, position: Int)
    fun regenerate()
    fun next(): AudioUi
    fun previous(): AudioUi
    var isLoop: Boolean
    var isRandom: Boolean

    fun isLast(): Boolean
    fun isFirst(): Boolean

    class Base(private val storage: SimpleStorage) : ManageOrder {
        private val actualOrder = mutableListOf<AudioUi>()
        private var actualPosition = 0
        private val cachedOrder = mutableListOf<AudioUi>()
        private lateinit var actualTrack: AudioUi
        override var isLoop: Boolean = false
            set(value) {
                storage.save(LOOP_KEY, value)
                field = value
            }
        override var isRandom: Boolean = false
            set(value) {
                storage.save(RANDOM_KEY, value)
                field = value
                regenerate()
                actualPosition = if (value) {
                    0
                } else try {
                    cachedOrder.indexOf(actualTrack)
                } catch (e: Exception) {
                    0
                }
            }

        override fun isLast() = actualPosition == actualOrder.lastIndex
        override fun isFirst() = actualPosition == 0

        override fun generate(tracks: List<AudioUi>, position: Int) {
            isLoop = storage.read(LOOP_KEY, false)
            isRandom = storage.read(RANDOM_KEY, false)

            cachedOrder.clear()
            cachedOrder.addAll(tracks)

            actualOrder.clear()
            if (isRandom) {
                val newOrder = ArrayList(cachedOrder)
                val actual = newOrder.removeAt(position)
                newOrder.shuffle()
                newOrder.add(0, actual)
                actualOrder.addAll(newOrder)
            } else {
                actualOrder.addAll(cachedOrder)
            }
            actualPosition = if (isRandom) 0 else position
            actualTrack = actualOrder[actualPosition]
        }

        override fun regenerate() {
            if (cachedOrder.isEmpty()) return
            actualOrder.clear()
            if (isRandom) {
                val newOrder = ArrayList(cachedOrder)
                val actual = newOrder.removeAt(actualPosition)
                newOrder.shuffle()
                newOrder.add(0, actual)
                actualOrder.addAll(newOrder)
                actualPosition = 0
                actualTrack = actualOrder[actualPosition]
            } else {
                actualOrder.addAll(cachedOrder)
                actualPosition = cachedOrder.indexOf(actualTrack)
            }
        }

        override fun next(): AudioUi {
            if (actualPosition == actualOrder.lastIndex && !isLoop)
                return actualOrder[actualPosition]
            else if (actualPosition == actualOrder.lastIndex && isLoop)
                actualPosition = -1
            actualTrack = actualOrder[++actualPosition]
            return actualTrack
        }

        override fun previous(): AudioUi {
            actualTrack = if (actualPosition == 0 && isLoop) {
                actualPosition = actualOrder.lastIndex
                actualOrder[actualPosition]
            } else if (actualPosition == 0) {
                actualOrder[actualPosition]
            } else
                actualOrder[--actualPosition]
            return actualTrack
        }

        companion object {
            private const val RANDOM_KEY = "RANDOM_KEY"
            private const val LOOP_KEY = "LOOP_KEY"
        }
    }
}