package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
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

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val navigation: Navigation.Update,
    private val manageOrder: ManageOrder,
    moreStorage: MoreStorage.Save,
    private val favoriteListRepository: FavoriteListRepository,
    runAsync: RunAsync = RunAsync.Base()
) : AbstractListViewModel(manageOrder, moreStorage, navigation, runAsync), Communication.Observe<AudioListState>, Reload {
    private var actualPosition = -1

    fun init(isFirstRun: Boolean, owner: LifecycleOwner) {
        handle({ interactor.data() }) { list ->
            communication.update(
                AudioListState.List(list.map { it.map(mapper) }, actualPosition, false)
            )
            manageOrder.init(list.subList(1, list.size).map { it.map(mapper) })
        }
        if (isFirstRun) {
            favoriteListRepository.init(this, owner)
        }
    }

    fun refresh(refreshFinish: RefreshFinish) {
        handle({ interactor.data() }) { list ->
            communication.update(AudioListState.List(list.map { it.map(mapper) }, actualPosition, false))
            refreshFinish.finish()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun setPosition(position: Int, orderType: OrderType) {
        actualPosition = if (orderType != OrderType.Base) -1 else position

        handle({ interactor.cachedData() }) { list ->
            communication.update(
                AudioListState.List(list.map { it.map(mapper) }, actualPosition, false)
            )
        }
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        actualPosition = position
        handle({ interactor.cachedData() }) { list ->
            mediaService.open(
                list.map { it.map(mapper) }.subList(1, list.size),
                track,
                position,
                OrderType.Base
            )
            navigation.update(PlayerScreen)
        }
    }

    override fun reload() {
        handle({ interactor.data().map { it.map(mapper) } }) { list ->
            communication.update(
                AudioListState.List(list, actualPosition, false)
            )
        }
    }
}