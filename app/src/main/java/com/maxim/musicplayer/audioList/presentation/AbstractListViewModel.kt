package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.RunAsync
import com.maxim.musicplayer.media.ManageOrder
import com.maxim.musicplayer.media.OrderType
import com.maxim.musicplayer.trackMore.presentation.MoreScreen
import com.maxim.musicplayer.trackMore.presentation.MoreStorage

abstract class AbstractListViewModel(
    private val manageOrder: ManageOrder,
    private val moreStorage: MoreStorage.Save,
    private val navigation: Navigation.Update,
    runAsync: RunAsync
): BaseViewModel(runAsync) {

    fun observePosition(owner: LifecycleOwner, observer: Observer<Pair<Int, OrderType>>) {
        manageOrder.observePosition(owner, observer)
    }

    fun more(audioUi: AudioUi) {
        moreStorage.saveAudio(audioUi)
        moreStorage.saveFromFavorite(true)
        navigation.update(MoreScreen)
    }
}