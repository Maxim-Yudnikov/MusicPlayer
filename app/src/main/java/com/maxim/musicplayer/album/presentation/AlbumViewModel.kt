package com.maxim.musicplayer.album.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.album.data.OpenAlbumStorage
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Reload
import com.maxim.musicplayer.core.presentation.Screen
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.media.ManageOrder
import com.maxim.musicplayer.media.MediaService
import com.maxim.musicplayer.media.OrderType
import com.maxim.musicplayer.player.presentation.PlayerScreen
import com.maxim.musicplayer.trackMore.presentation.MoreScreen
import com.maxim.musicplayer.trackMore.presentation.MoreStorage

class AlbumViewModel(
    private val communication: AlbumCommunication,
    private val storage: OpenAlbumStorage.Mutable,
    private val manageOrder: ManageOrder,
    private val moreStorage: MoreStorage.Save,
    private val favoritesRepository: FavoriteListRepository,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
) : BaseViewModel(), GoBack, Communication.Observe<AlbumState>, Reload {
    private var actualPosition = -1

    fun init(isFirstRun: Boolean, owner: LifecycleOwner) {
        if (isFirstRun) {
            communication.update(AlbumState.Base(storage.read(), -1))
            favoritesRepository.init(this, owner)
        }
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(AlbumViewModel::class.java)
    }

    fun setPosition(position: Int, orderType: OrderType) {
        actualPosition = if (orderType.same(storage.read())) position + 1 else -1

        communication.update(
            AlbumState.Base(storage.read(), actualPosition)
        )
    }

    fun more(audioUi: AudioUi) {
        moreStorage.saveAudio(audioUi)
        moreStorage.saveFromFavorite(false)
        navigation.update(MoreScreen)
    }

    fun observePosition(owner: LifecycleOwner, observer: Observer<Pair<Int, OrderType>>) {
        manageOrder.observePosition(owner, observer)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        storage.saveActual(storage.read())
        actualPosition = position + 1
        storage.read().open(mediaService, track, position, OrderType.Album(storage.read()))
        navigation.update(PlayerScreen)
    }

    override fun reload() {
        storage.save(
            storage.read()
                .updateTracks(favoritesRepository.data()
                    .map {
                        it.map(mapper)
                    })
        )
        communication.update(AlbumState.Base(storage.read(), actualPosition))

    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AlbumState>) {
        communication.observe(owner, observer)
    }
}