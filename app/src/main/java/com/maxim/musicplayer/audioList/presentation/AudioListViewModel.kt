package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Init
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Reload
import com.maxim.musicplayer.core.presentation.RunAsync
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.presentation.PlayerScreen
import com.maxim.musicplayer.trackMore.presentation.MoreScreen
import com.maxim.musicplayer.trackMore.presentation.MoreStorage

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val navigation: Navigation.Update,
    private val manageOrder: ManageOrder,
    private val moreStorage: MoreStorage.Save,
    private val favoriteListRepository: FavoriteListRepository,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, Communication.Observe<AudioListState>, Reload {
    private var actualPosition = -1

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            handle({ interactor.data().map { it.map(mapper) } }) { list ->
                communication.update(
                    AudioListState.List(list, actualPosition)
                )
            }
            favoriteListRepository.init(this)
        }
    }

    fun refresh(refreshFinish: RefreshFinish) {
        handle({ interactor.data() }) { list ->
            communication.update(AudioListState.List(list.map { it.map(mapper) }, actualPosition))
            refreshFinish.finish()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun setPosition(position: Int) {
        actualPosition = position
        handle({interactor.cachedData()}) { list ->
            communication.update(
                AudioListState.List(list.map { it.map(mapper) }, position)
            )
        }
    }

    //todo make abstract list viewmodel
    fun more(audioUi: AudioUi) {
        moreStorage.saveAudio(audioUi)
        moreStorage.saveFromFavorite(false)
        navigation.update(MoreScreen)
    }

    fun observePosition(owner: LifecycleOwner, observer: Observer<Int>) {
        manageOrder.observeActualTrackPosition(owner, observer)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        manageOrder.setActualTrack(position)
        actualPosition = position
        handle({ interactor.cachedData() }) { list ->
            mediaService.open(list.map { it.map(mapper) }
                .subList(1, list.size) as List<AudioUi.Abstract>, track, position, false)
            navigation.update(PlayerScreen)
        }
    }

    override fun reload() {
        handle({ interactor.data() }) { list ->
            communication.update(
                AudioListState.List(list.map { it.map(mapper) }, actualPosition)
            )
        }
    }
}