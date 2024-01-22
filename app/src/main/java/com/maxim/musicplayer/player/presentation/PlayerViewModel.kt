package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Screen
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.order.presentation.OrderScreen
import com.maxim.musicplayer.media.ManageOrder
import com.maxim.musicplayer.media.Playable

class PlayerViewModel(
    private val downBarTrackCommunication: DownBarTrackCommunication,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    private val favoriteListRepository: FavoriteListRepository
) : BaseViewModel(), Communication.Observe<PlayerState>, Playable, GoBack {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val track = manageOrder.actualTrack()
            downBarTrackCommunication.setTrack(
                track,
                mediaServiceProvider.mediaService(),
                mediaServiceProvider.mediaService().isPlaying()
            )
            communication.update(
                PlayerState.Base(
                    manageOrder.actualTrack(),
                    manageOrder.isRandom(),
                    manageOrder.loopState(),
                    !mediaServiceProvider.mediaService().isPlaying(),
                    mediaServiceProvider.mediaService().currentPosition()
                )
            )
        }
    }

    fun order() {
        navigation.update(OrderScreen)
    }

    fun saveToFavorites() {
        handle({
            manageOrder.actualTrack().changeFavorite(favoriteListRepository)
        }) {
            val isLast = manageOrder.changeActualFavorite(mediaServiceProvider.mediaService())
            if (!isLast)
                communication.update(
                    PlayerState.Base(
                        manageOrder.actualTrack(),
                        manageOrder.isRandom(),
                        manageOrder.loopState(),
                        !mediaServiceProvider.mediaService().isPlaying(),
                        mediaServiceProvider.mediaService().currentPosition()
                    )
                )
        }
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(PlayerViewModel::class.java)
    }

    override fun play() {
        mediaServiceProvider.mediaService().play()
    }

    override fun next() {
        mediaServiceProvider.mediaService().next()
    }

    override fun previous() {
        mediaServiceProvider.mediaService().previous()
    }

    override fun finish() = Unit

    fun changeRandom() {
        mediaServiceProvider.mediaService().changeRandom()
    }

    fun changeLoop() {
        mediaServiceProvider.mediaService().changeLoop()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PlayerState>) {
        communication.observe(owner, observer)
    }
}