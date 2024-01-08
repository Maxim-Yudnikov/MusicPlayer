package com.maxim.musicplayer.favoriteList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.presentation.AudioListCommunication
import com.maxim.musicplayer.audioList.presentation.AudioListState
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.presentation.BaseViewModel
import com.maxim.musicplayer.cope.presentation.Communication
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.cope.presentation.Reload
import com.maxim.musicplayer.cope.presentation.RunAsync
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.presentation.PlayerScreen
import com.maxim.musicplayer.trackMore.presentation.MoreScreen

class FavoriteListViewModel(
    private val repository: FavoriteListRepository,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val audioListCommunication: AudioListCommunication,
    private val manageOrder: ManageOrder,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<AudioListState>, Reload {
    private var actualPosition = -1

    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            repository.init(this)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        audioListCommunication.observe(owner, observer)
    }

    fun observePosition(owner: LifecycleOwner, observer: Observer<Int>) {
        manageOrder.observeActualTrackFavoritePosition(owner, observer)
    }

    fun setPosition(position: Int) {
        actualPosition = position
        audioListCommunication.update(
            AudioListState.List(
                repository.data().map { it.map(mapper) },
                position
            )
        )
    }

    fun more(audioUi: AudioUi) {
        navigation.update(MoreScreen)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        manageOrder.setActualTrackFavorite(position)
        actualPosition = position
        val data = repository.data()
        mediaService.open(data.map { it.map(mapper) }
            .subList(1, data.lastIndex) as List<AudioUi.Abstract>, track, position, true)
        navigation.update(PlayerScreen)
    }

    override fun reload() {
        val list = repository.data().map { it.map(mapper) }
        audioListCommunication.update(AudioListState.List(list, actualPosition))
    }
}