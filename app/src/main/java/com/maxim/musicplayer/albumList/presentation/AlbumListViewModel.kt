package com.maxim.musicplayer.albumList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.album.data.OpenAlbumStorage
import com.maxim.musicplayer.album.presentation.AlbumScreen
import com.maxim.musicplayer.albumList.data.AlbumDomain
import com.maxim.musicplayer.albumList.data.AlbumListRepository
import com.maxim.musicplayer.audioList.presentation.RefreshFinish
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Reload
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository

class AlbumListViewModel(
    private val communication: AlbumListCommunication,
    private val repository: AlbumListRepository,
    private val storage: OpenAlbumStorage.Save,
    private val mapper: AlbumDomain.Mapper<AlbumUi>,
    private val favoritesRepository: FavoriteListRepository,
    private val navigation: Navigation.Update
) : BaseViewModel(), Communication.Observe<AlbumListState>, Reload {

    fun init(isFirstRun: Boolean, owner: LifecycleOwner) {
        if (isFirstRun) {
            communication.update(AlbumListState.Base(repository.data().map { it.map(mapper) }))
            favoritesRepository.init(this, owner)
        }
    }

    fun refresh(refreshFinish: RefreshFinish) {
        handle({repository.data()}) { list ->
            communication.update(AlbumListState.Base(list.map { it.map(mapper) }))
            refreshFinish.finish()
        }
    }

    fun open(albumUi: AlbumUi) {
        storage.save(albumUi)
        navigation.update(AlbumScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AlbumListState>) {
        communication.observe(owner, observer)
    }

    override fun reload() {
        communication.update(AlbumListState.Base(repository.data().map { it.map(mapper) }))
    }
}