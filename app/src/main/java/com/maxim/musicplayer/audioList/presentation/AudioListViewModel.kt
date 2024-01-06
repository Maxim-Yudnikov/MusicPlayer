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
import com.maxim.musicplayer.player.presentation.OpenPlayerStorage
import com.maxim.musicplayer.player.presentation.PlayerScreen

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val sharedStorage: OpenPlayerStorage.Save,
    private val navigation: Navigation.Update,
    private val manageOrder: ManageOrder,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, Communication.Observe<AudioListState> {
    private var refreshFinish: RefreshFinish? = null
    private var isRefreshing = false
    override fun init(isFirstRun: Boolean) {
        communication.update(AudioListState.List(interactor.data().map { it.map(mapper) }))
        isRefreshing = true
        handle({ interactor.dataWithImages() }) { list ->
            communication.update(AudioListState.List(list.map { it.map(mapper) }))
            refreshFinish?.finish()
            refreshFinish = null
            isRefreshing = false
        }
    }

    fun refresh(refreshFinish: RefreshFinish) {
        if (isRefreshing) this.refreshFinish = refreshFinish
        else handle({ interactor.dataWithImages() }) { list ->
            communication.update(AudioListState.List(list.map { it.map(mapper) }))
            refreshFinish.finish()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun open(audio: AudioUi, position: Int) {
        sharedStorage.save(audio)
        val list = interactor.cachedData().map { it.map(mapper) }
        manageOrder.generate(list.subList(1, list.lastIndex), position)
        navigation.update(PlayerScreen)
    }
}