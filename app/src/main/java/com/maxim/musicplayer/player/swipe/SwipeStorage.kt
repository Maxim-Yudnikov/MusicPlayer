package com.maxim.musicplayer.player.swipe

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication

interface SwipeStorage: Communication.Observe<Triple<AudioUi?, AudioUi?, AudioUi?>> {
    fun update(position: Int, audioUi: AudioUi)

    class Base(
        private val liveData: MutableLiveData<Triple<AudioUi?, AudioUi?, AudioUi?>>
    ): SwipeStorage {
        override fun update(position: Int, audioUi: AudioUi) {
            when (position) {
                0 -> liveData.value = Triple(audioUi, liveData.value?.second, liveData.value?.third)
                1 -> liveData.value = Triple(liveData.value?.first, audioUi, liveData.value?.third)
                2 -> liveData.value = Triple(liveData.value?.first, liveData.value?.second, audioUi)
            }
        }

        override fun observe(
            owner: LifecycleOwner,
            observer: Observer<Triple<AudioUi?, AudioUi?, AudioUi?>>
        ) {
            liveData.observe(owner, observer)
        }
    }
}