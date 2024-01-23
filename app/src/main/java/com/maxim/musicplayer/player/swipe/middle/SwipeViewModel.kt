package com.maxim.musicplayer.player.swipe.middle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.player.swipe.SwipeStorage

class SwipeViewModel(
    private val swipeStorage: SwipeStorage
): ViewModel(), Communication.Observe<Triple<AudioUi?, AudioUi?, AudioUi?>> {

    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<Triple<AudioUi?, AudioUi?, AudioUi?>>
    ) {
        swipeStorage.observe(owner, observer)
    }
}