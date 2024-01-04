package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Init

class PlayerViewModel(
    private val sharedStorage: OpenPlayerStorage.Read,
    private val communication: PlayerCommunication
): ViewModel(), Init, Communication.Observe<PlayerState> {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(PlayerState.Running(sharedStorage.read()))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }
}