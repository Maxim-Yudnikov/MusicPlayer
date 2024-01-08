package com.maxim.musicplayer.trackMore.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.media.ManageOrder

class MoreViewModel(
    private val communication: MoreCommunication,
    private val storage: MoreStorage.Mutable,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val favoriteListRepository: FavoriteListRepository
): BaseViewModel(), Communication.Observe<MoreState> {

    fun init() {
        communication.update(MoreState.Base(storage.read()))
    }

    fun saveToFavorites() {
        handle({
            storage.read().changeFavorite(favoriteListRepository)
        }) {
            manageOrder.changeFavorite((storage.read() as AudioUi.Abstract).id, mediaServiceProvider.mediaService())
            storage.save(storage.read().changeFavorite())
            communication.update(MoreState.Base(storage.read()))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MoreState>) {
        communication.observe(owner, observer)
    }
}