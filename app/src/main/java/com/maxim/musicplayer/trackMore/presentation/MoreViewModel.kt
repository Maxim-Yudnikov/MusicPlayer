package com.maxim.musicplayer.trackMore.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.media.ManageOrder

class MoreViewModel( //todo clearViewModel
    private val communication: MoreCommunication,
    private val storage: MoreStorage.Mutable,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val favoriteListRepository: FavoriteListRepository,
): BaseViewModel(), Communication.Observe<MoreState> {

    fun init() {
        communication.update(MoreState.Base(storage.readAudio()))
    }

    fun fromFavorite() = storage.fromFavorite()

    fun saveToFavorites() {
        handle({
            storage.readAudio().changeFavorite(favoriteListRepository)
        }) {
            manageOrder.changeFavorite((storage.readAudio() as AudioUi.Abstract).id, mediaServiceProvider.mediaService())
            storage.saveAudio(storage.readAudio().changeFavorite())
            communication.update(MoreState.Base(storage.readAudio()))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MoreState>) {
        communication.observe(owner, observer)
    }
}