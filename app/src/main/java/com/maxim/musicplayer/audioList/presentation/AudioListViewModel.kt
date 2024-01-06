package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.cope.BaseViewModel
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Init
import com.maxim.musicplayer.cope.Navigation
import com.maxim.musicplayer.cope.RunAsync
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.presentation.PlayerScreen

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val navigation: Navigation.Update,
    private val manageOrder: ManageOrder,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, Communication.Observe<AudioListState> {
    private var refreshFinish: RefreshFinish? = null
    private var isRefreshing = false
    private var actualPosition = -1

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(
                AudioListState.List(interactor.dataWithImages().map { it.map(mapper) }, actualPosition)
            )
        }
    }

    fun refresh(refreshFinish: RefreshFinish) {
        if (isRefreshing) this.refreshFinish = refreshFinish
        else handle({ interactor.dataWithImages() }) { list ->
            communication.update(AudioListState.List(list.map { it.map(mapper) }, actualPosition))
            refreshFinish.finish()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun setPosition(position: Int) {
        communication.update(
            AudioListState.List(interactor.cachedData().map { it.map(mapper) }, position)
        )
    }

    fun observePosition(owner: LifecycleOwner, observer: Observer<Int>) {
        manageOrder.observeActualTrackPosition(owner, observer)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        manageOrder.setActualTrack(position)
        actualPosition = position
        val list = interactor.cachedData().map { it.map(mapper) }
        mediaService.open(list.subList(1, list.lastIndex), track, position)
        navigation.update(PlayerScreen)
    }
}