package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.cope.presentation.BaseViewModel
import com.maxim.musicplayer.cope.presentation.Communication
import com.maxim.musicplayer.cope.presentation.Init
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.cope.presentation.RunAsync
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
            handle({ interactor.dataWithImages() }) { list ->
                communication.update(
                    AudioListState.List(list.map { it.map(mapper) }, actualPosition)
                )
            }
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
        handle({interactor.cachedData()}) { list ->
            communication.update(
                AudioListState.List(list.map { it.map(mapper) }, position)
            )
        }
    }

    fun observePosition(owner: LifecycleOwner, observer: Observer<Int>) {
        manageOrder.observeActualTrackPosition(owner, observer)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        manageOrder.setActualTrack(position)
        actualPosition = position
        handle({ interactor.cachedData() }) { list ->
            mediaService.open(list.map { it.map(mapper) }
                .subList(1, list.lastIndex) as List<AudioUi.Abstract>, track, position)
            navigation.update(PlayerScreen)
        }
    }
}