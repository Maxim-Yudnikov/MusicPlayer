package com.maxim.musicplayer.trackMore.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.ShowError
import com.maxim.musicplayer.details.presentation.DetailsScreen
import com.maxim.musicplayer.details.presentation.DetailsStorage
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.media.ManageOrder

class MoreViewModel( //todo clearViewModel
    private val communication: MoreCommunication,
    private val storage: MoreStorage.Mutable,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val favoriteListRepository: FavoriteListRepository,
    private val detailsStorage: DetailsStorage.Save,
    private val navigation: Navigation.Update,
): BaseViewModel(), Communication.Observe<MoreState> {

    fun init() {
        communication.update(MoreState.Base(storage.readAudio()))
    }

    fun fromFavorite() = storage.fromFavorite()

    fun playNext() {
        manageOrder.playNext(storage.readAudio())
    }

    fun details() {
        detailsStorage.save(storage.readAudio().id())
        navigation.update(DetailsScreen)
    }

    fun saveToFavorites(showError: ShowError) {
        if (storage.readAudio() is AudioUi.Base && !manageOrder.canAddToFavorites())
            return showError.show("You can't save a track to favorites while listening to your favorite tracks")

        handle({
            storage.readAudio().changeFavorite(favoriteListRepository)
        }) {
            manageOrder.changeFavorite(storage.readAudio().id(), mediaServiceProvider.mediaService())
            storage.saveAudio(storage.readAudio().changeFavorite())
            communication.update(MoreState.Base(storage.readAudio()))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MoreState>) {
        communication.observe(owner, observer)
    }
}