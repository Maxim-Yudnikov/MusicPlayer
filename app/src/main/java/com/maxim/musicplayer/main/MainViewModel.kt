package com.maxim.musicplayer.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.cope.presentation.Communication
import com.maxim.musicplayer.cope.presentation.Init
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.cope.presentation.Screen
import com.maxim.musicplayer.player.presentation.PlayerScreen

class MainViewModel(
    private val navigation: Navigation.Mutable
) : ViewModel(), Init, Communication.Observe<Screen> {
    override fun init(isFirstRun: Boolean) {
        //if (isFirstRun)
            //navigation.update(AudioListScreen)
    }

    fun openPlayer() {
        navigation.update(PlayerScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) {
        navigation.observe(owner, observer)
    }
}