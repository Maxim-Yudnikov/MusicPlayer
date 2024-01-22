package com.maxim.musicplayer.player.media

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.data.SimpleStorage

interface ManageOrder {
    fun init(allTracks: List<AudioUi>)

    fun generate(tracks: List<AudioUi>, position: Int, orderType: OrderType)
    fun regenerate()
    fun next(): AudioUi
    fun previous(): AudioUi
    fun initLoop(mediaPlayer: MediaPlayer)

    fun changeLoop(mediaPlayer: MediaPlayer?)
    fun loopState(): LoopState
    fun changeRandom()
    fun isRandom(): Boolean

    fun canGoNext(): Boolean
    fun canGoPrevious(): Boolean
    fun canAddToFavorites(): Boolean

    fun actualTrack(): AudioUi
    fun actualOrder(): List<AudioUi>
    fun actualPosition(): Int
    fun removeTrackFromActualOrder(id: Long)

    fun observePosition(owner: LifecycleOwner, observer: Observer<Pair<Int, OrderType>>)

    fun changeActualFavorite(playable: Playable): Boolean
    fun changeFavorite(id: Long, playable: Playable)

    fun playNext(audioUi: AudioUi)

    class Base(private val storage: SimpleStorage, private val shuffleOrder: ShuffleOrder) :
        ManageOrder {
        private var isRandom = storage.read(RANDOM_KEY, false)
        private var loopState: LoopState = storage.read(LOOP_KEY, LoopState.Base)

        private val tracksMap = mutableMapOf<Long, AudioUi>()
        private val defaultOrder = mutableListOf<Long>()
        private val actualOrder = mutableListOf<Long>()
        private var actualPosition = 0

        private val absolutePositionLiveData = MutableLiveData<Pair<Int, OrderType>>()

        override fun loopState() = loopState
        override fun isRandom() = isRandom
        override fun actualOrder() = actualOrder.map { tracksMap[it]!! }
        override fun actualPosition() = actualPosition
        override fun actualTrack() = tracksMap[actualOrder[actualPosition]]!!

        override fun changeLoop(mediaPlayer: MediaPlayer?) {
            loopState = loopState().next()
            storage.save(LOOP_KEY, loopState)
            mediaPlayer?.let {
                loopState.handle(mediaPlayer)
            }
        }

        override fun initLoop(mediaPlayer: MediaPlayer) {
            loopState.handle(mediaPlayer)
        }

        override fun changeRandom() {
            isRandom = !isRandom
            storage.save(RANDOM_KEY, isRandom)
            regenerate()
        }

        override fun init(allTracks: List<AudioUi>) {
            tracksMap.clear()
            allTracks.forEach {
                tracksMap[it.id()] = it
            }
        }

        override fun generate(
            tracks: List<AudioUi>,
            position: Int,
            orderType: OrderType
        ) {
            defaultOrder.clear()
            defaultOrder.addAll(tracks.map { it.id() })

            actualOrder.clear()
            actualPosition = if (isRandom) {
                val newOrder = ArrayList(shuffleOrder.shuffle(defaultOrder))
                val actualTrack =
                    newOrder.removeAt(newOrder.indexOf(defaultOrder[position]))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(defaultOrder)
                position
            }
            absolutePositionLiveData.value = Pair(position, orderType)
        }

        override fun regenerate() {
            val cachedActualTrack = actualOrder[actualPosition]

            actualOrder.clear()
            actualPosition = if (isRandom) {
                val newOrder = ArrayList(shuffleOrder.shuffle(defaultOrder))
                val actualTrack =
                    newOrder.removeAt(newOrder.indexOf(cachedActualTrack))
                newOrder.add(0, actualTrack)
                actualOrder.addAll(newOrder)
                0
            } else {
                actualOrder.addAll(defaultOrder)
                defaultOrder.indexOf(cachedActualTrack)
            }
        }

        override fun next(): AudioUi {
            if (actualPosition != actualOrder.lastIndex) {
                actualPosition++
            } else if (loopState == LoopState.LoopOrder) {
                actualPosition = 0
            }
            absolutePositionLiveData.value =
                Pair(
                    defaultOrder.indexOf(actualOrder[actualPosition]),
                    absolutePositionLiveData.value!!.second
                )
            return tracksMap[actualOrder[actualPosition]]!!
        }

        override fun previous(): AudioUi {
            if (actualPosition != 0) {
                actualPosition--
            } else if (loopState == LoopState.LoopOrder) {
                actualPosition = actualOrder.lastIndex
            }
            absolutePositionLiveData.value =
                Pair(
                    defaultOrder.indexOf(actualOrder[actualPosition]),
                    absolutePositionLiveData.value!!.second
                )
            return tracksMap[actualOrder[actualPosition]]!!
        }

        override fun canGoNext() =
            (actualPosition != actualOrder.lastIndex || loopState == LoopState.LoopOrder) && actualOrder.size > 1

        override fun canGoPrevious() =
            (actualPosition != 0 || loopState == LoopState.LoopOrder) && actualOrder.size > 1

        override fun canAddToFavorites() =
            absolutePositionLiveData.value?.second != OrderType.Favorite

        override fun removeTrackFromActualOrder(id: Long) {
            val actualTrack = actualOrder[actualPosition]
            actualOrder.remove(id)
            actualPosition = actualOrder.indexOf(actualTrack)
            absolutePositionLiveData.value =
                Pair(defaultOrder.indexOf(actualTrack), absolutePositionLiveData.value!!.second)
        }

        override fun playNext(audioUi: AudioUi) {
            if (actualOrder.isEmpty())
                return

            val id = audioUi.id()
            if (id == actualOrder[actualPosition])
                return
            if (actualOrder.contains(id))
                actualOrder.remove(id)
            actualOrder.add(actualPosition + 1, id)
        }

        override fun observePosition(
            owner: LifecycleOwner,
            observer: Observer<Pair<Int, OrderType>>
        ) {
            absolutePositionLiveData.observe(owner, observer)
        }

        override fun changeActualFavorite(playable: Playable): Boolean {
            val isFavoriteOrder = absolutePositionLiveData.value?.second == OrderType.Favorite
            val newTrack = tracksMap[actualOrder[actualPosition]]!!.changeFavorite()
            tracksMap[actualOrder[actualPosition]] = newTrack
            if (isFavoriteOrder && newTrack is AudioUi.Base) {
                val removed = actualOrder.removeAt(actualPosition)
                defaultOrder.removeAt(defaultOrder.indexOf(removed))
                actualPosition--
                if (actualOrder.isEmpty()) {
                    playable.finish()
                    return true
                } else
                    playable.next()
            }
            return false
        }

        override fun changeFavorite(id: Long, playable: Playable) {
            val isFavoriteOrder = absolutePositionLiveData.value?.second == OrderType.Favorite
            tracksMap[id]?.let { track ->
                val newTrack = track.changeFavorite()
                tracksMap[id] = newTrack
                if (isFavoriteOrder && newTrack is AudioUi.Base) {
                    val actualTrack = actualOrder[actualPosition]
                    actualOrder.remove(id)
                    defaultOrder.remove(id)
                    actualPosition = actualOrder.indexOf(actualTrack)
                    if (actualOrder.isEmpty()) {
                        playable.finish()
                        absolutePositionLiveData.value = Pair(-1, OrderType.Empty)
                    } else if (id == actualTrack)
                        playable.next()
                }
                absolutePositionLiveData.value = Pair(
                    if (actualOrder.isNotEmpty()) defaultOrder.indexOf(actualOrder[actualPosition]) else -1,
                    absolutePositionLiveData.value?.second ?: OrderType.Empty
                )
            }
        }

        companion object {
            private const val RANDOM_KEY = "RANDOM_KEY"
            private const val LOOP_KEY = "LOOP_KEY"
        }
    }
}