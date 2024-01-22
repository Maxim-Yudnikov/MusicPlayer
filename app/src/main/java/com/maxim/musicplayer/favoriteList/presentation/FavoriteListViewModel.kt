package com.maxim.musicplayer.favoriteList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.presentation.AbstractListViewModel
import com.maxim.musicplayer.audioList.presentation.AudioListCommunication
import com.maxim.musicplayer.audioList.presentation.AudioListState
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Reload
import com.maxim.musicplayer.core.presentation.RunAsync
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.media.ManageOrder
import com.maxim.musicplayer.media.MediaService
import com.maxim.musicplayer.media.OrderType
import com.maxim.musicplayer.player.presentation.PlayerScreen
import com.maxim.musicplayer.trackMore.presentation.MoreStorage

class FavoriteListViewModel(
    private val repository: FavoriteListRepository,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val audioListCommunication: AudioListCommunication,
    manageOrder: ManageOrder,
    private val navigation: Navigation.Update,
    moreStorage: MoreStorage.Save,
    runAsync: RunAsync = RunAsync.Base()
) : AbstractListViewModel(manageOrder, moreStorage, navigation, runAsync), Communication.Observe<AudioListState>, Reload {
    private var actualPosition = -1

    fun init(isFirstRun: Boolean, owner: LifecycleOwner) {
        if (isFirstRun)
            repository.init(this, owner)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        audioListCommunication.observe(owner, observer)
    }

    fun setPosition(position: Int, orderType: OrderType) {
        actualPosition = if (orderType != OrderType.Favorite) -1 else position

        audioListCommunication.update(
            AudioListState.List(
                repository.data().map { it.map(mapper) },
                actualPosition, false
            )
        )
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        actualPosition = position
        val data = repository.data()
        mediaService.open(data.map { it.map(mapper) }
            .subList(1, data.size), track, position, OrderType.Favorite)
        navigation.update(PlayerScreen)
    }

    override fun reload() {
        val list = repository.data().map { it.map(mapper) }
        audioListCommunication.update(AudioListState.List(list, actualPosition, true))
    }
}