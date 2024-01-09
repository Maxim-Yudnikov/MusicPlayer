package com.maxim.musicplayer.player.media

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.musicplayer.albumList.presentation.AlbumUi
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.data.SimpleStorage
import com.maxim.musicplayer.core.presentation.Reload

interface ManageOrder {
    fun generate(tracks: List<AudioUi.Abstract>, position: Int)
    fun regenerate()
    fun next(): AudioUi
    fun previous(): AudioUi
    fun initLoop(mediaPlayer: MediaPlayer)
    fun changeLoop(mediaPlayer: MediaPlayer?)
    fun loopState(): LoopState
    var isRandom: Boolean

    fun canGoNext(): Boolean
    fun canGoPrevious(): Boolean

    fun actualTrack(): AudioUi
    fun actualOrder(): List<AudioUi>
    fun actualPosition(): Int
    fun actualAbsolutePosition(): Int
    fun removeTrackFromActualOrder(id: Long)

    fun setActualTrack(position: Int)
    fun setActualTrackFavorite(position: Int)
    fun setActualAlbumTrack(position: Int, albumUi: AlbumUi?)
    fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>)
    fun observeActualTrackFavoritePosition(owner: LifecycleOwner, observer: Observer<Int>)
    fun observeActualAlbumTrackPosition(
        owner: LifecycleOwner,
        observer: Observer<Pair<Int, AlbumUi>>
    )
    fun observeAnyPosition(reload: Reload)

    fun changeActualFavorite(playable: Playable): Boolean
    fun changeFavorite(id: Long, playable: Playable)

    class Base(private val storage: SimpleStorage, private val shuffleOrder: ShuffleOrder) :
        ManageOrder {
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
        private val actualTrackFavoritePositionLiveData = MutableLiveData<Int>()

        override fun generate(tracks: List<AudioUi.Abstract>, position: Int) {
            defaultOrder.clear()
            defaultOrder.addAll(tracks.map { it.id })
            trackMap.clear()
            tracks.forEach {
                trackMap[it.id] = it
            }
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
        }

        override fun regenerate() {
            val cachedActualTrack = actualOrder[actualPosition]
            actualOrder.clear()

            actualPosition = if (isRandom) {
                val newOrder = ArrayList(shuffleOrder.shuffle(defaultOrder))
                val actualTrack = newOrder.removeAt(newOrder.indexOf(cachedActualTrack))
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
        override fun removeTrackFromActualOrder(id: Long) {
            val actualTrack = actualOrder[actualPosition]
            actualOrder.remove(id)
            actualPosition = actualOrder.indexOf(actualTrack)
        }

        override fun setActualTrack(position: Int) {
            actualTrackPositionLiveData.value = position
            actualTrackFavoritePositionLiveData.value = -1
            actualAlbumTrackLiveData.value = Pair(-1, AlbumUi.Empty)
            anyPositionReload?.reload()
        }

        override fun setActualTrackFavorite(position: Int) {
            actualTrackFavoritePositionLiveData.value = position
            actualTrackPositionLiveData.value = -1
            actualAlbumTrackLiveData.value = Pair(-1, AlbumUi.Empty)
            anyPositionReload?.reload()
        }

        private val actualAlbumTrackLiveData = MutableLiveData<Pair<Int, AlbumUi>>()

        override fun setActualAlbumTrack(position: Int, albumUi: AlbumUi?) {
            actualAlbumTrackLiveData.value = albumUi?.let { Pair(position, it) } ?: Pair(
                position,
                actualAlbumTrackLiveData.value!!.second
            )
            actualTrackFavoritePositionLiveData.value = -1
            actualTrackPositionLiveData.value = -1
            anyPositionReload?.reload()
        }

        override fun observeActualTrackPosition(owner: LifecycleOwner, observer: Observer<Int>) {
            actualTrackPositionLiveData.observe(owner, observer)
        }

        override fun observeActualTrackFavoritePosition(
            owner: LifecycleOwner,
            observer: Observer<Int>
        ) {
            actualTrackFavoritePositionLiveData.observe(owner, observer)
        }

        override fun observeActualAlbumTrackPosition(
            owner: LifecycleOwner,
            observer: Observer<Pair<Int, AlbumUi>>
        ) {
            actualAlbumTrackLiveData.observe(owner, observer)
        }

        private var anyPositionReload: Reload? = null
        override fun observeAnyPosition(reload: Reload) {
            anyPositionReload = reload
        }

        override fun initLoop(mediaPlayer: MediaPlayer) {
            loopState.handle(mediaPlayer)
        }

        override fun changeLoop(mediaPlayer: MediaPlayer?) {
            loopState = loopState.next()
            storage.save(LOOP_KEY, loopState)
            mediaPlayer?.let {
                loopState.handle(mediaPlayer)
            }
        }

        override fun changeActualFavorite(playable: Playable): Boolean {
            val isFavoriteOrder = actualTrackFavoritePositionLiveData.value != -1
            val newTrack = trackMap[actualOrder[actualPosition]]!!.changeFavorite()
            trackMap[actualOrder[actualPosition]] = newTrack
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

        private var lastRemovedPositionAndTrack = Pair(0, 0L)
        override fun changeFavorite(id: Long, playable: Playable) {
            val isFavoriteOrder = actualTrackFavoritePositionLiveData.value != -1
            trackMap[id]?.let { track ->
                val newTrack = track.changeFavorite()
                trackMap[id] = newTrack
                if (isFavoriteOrder && newTrack is AudioUi.Base) {
                    lastRemovedPositionAndTrack = Pair(actualOrder.indexOf(id), id)
                    actualOrder.remove(id)
                    defaultOrder.remove(id)
                    actualPosition--
                    if (actualOrder.isEmpty()) {
                        playable.finish()
                        actualTrackFavoritePositionLiveData.value = -1
                    } else
                        playable.next()
                }
            }
        }

        override fun actualOrder() = actualOrder.map { trackMap[it]!! }
        override fun actualPosition() = actualPosition

        override fun loopState() = loopState

        companion object {
            private const val RANDOM_KEY = "RANDOM_KEY"
            private const val LOOP_KEY = "LOOP_KEY"
        }
    }
}