package com.maxim.musicplayer.trackMore.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.details.presentation.DetailsScreen
import com.maxim.musicplayer.details.presentation.DetailsStorage
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.media.ManageOrder

class MoreViewModel(
    private val communication: MoreCommunication,
    private val storage: MoreStorage.Mutable,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val favoriteListRepository: FavoriteListRepository,
    private val detailsStorage: DetailsStorage.Save,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
): BaseViewModel(), Communication.Observe<MoreState>, GoBack {

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

    fun saveToFavorites() {
        handle({
            storage.readAudio().changeFavorite(favoriteListRepository)
            favoriteListRepository.singleDataIds()
        }) { list ->
            manageOrder.changeFavorite(storage.readAudio().id(), mediaServiceProvider.mediaService(), list)
            storage.saveAudio(storage.readAudio().changeFavorite())
            communication.update(MoreState.Base(storage.readAudio()))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MoreState>) {
        communication.observe(owner, observer)
    }

    override fun goBack() {
        clearViewModel.clear(MoreViewModel::class.java)
    }
}