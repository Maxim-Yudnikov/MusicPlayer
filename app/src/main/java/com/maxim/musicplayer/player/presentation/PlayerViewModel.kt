package com.maxim.musicplayer.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.cope.presentation.BaseViewModel
import com.maxim.musicplayer.cope.presentation.Communication
import com.maxim.musicplayer.cope.presentation.Navigation
import com.maxim.musicplayer.cope.presentation.Screen
import com.maxim.musicplayer.cope.sl.ClearViewModel
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.Playable

class PlayerViewModel(
    private val downBarTrackCommunication: DownBarTrackCommunication,
    private val communication: PlayerCommunication,
    private val manageOrder: ManageOrder,
    private val mediaServiceProvider: ProvideMediaService,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    private val favoriteListRepository: FavoriteListRepository
) : BaseViewModel(), Communication.Observe<PlayerState>, Playable {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val track = manageOrder.actualTrack()
            downBarTrackCommunication.setTrack(track, mediaServiceProvider.mediaService())
            communication.update(
                PlayerState.Base(
                    manageOrder.actualTrack(),
                    manageOrder.isRandom,
                    manageOrder.loopState(),
                    !mediaServiceProvider.mediaService().isPlaying(),
                    mediaServiceProvider.mediaService().currentPosition()
                )
            )
        }
    }

    fun saveToFavorites() {
        handle({
            manageOrder.actualTrack().changeFavorite(favoriteListRepository)
        }) {
            manageOrder.changeActualFavorite(mediaServiceProvider.mediaService())
            communication.update(
                PlayerState.Base(
                    manageOrder.actualTrack(),
                    manageOrder.isRandom,
                    manageOrder.loopState(),
                    !mediaServiceProvider.mediaService().isPlaying(),
                    mediaServiceProvider.mediaService().currentPosition()
                )
            )
        }
    }

    fun back() {
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