package com.maxim.musicplayer.album.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.album.data.OpenAlbumStorage
import com.maxim.musicplayer.albumList.presentation.AlbumUi
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Init
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Screen
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.presentation.PlayerScreen

class AlbumViewModel(
    private val communication: AlbumCommunication,
    private val storage: OpenAlbumStorage.Read,
    private val manageOrder: ManageOrder,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
) : ViewModel(), GoBack, Init, Communication.Observe<AlbumState> {

    override fun init(isFirstRun: Boolean) {
        communication.update(AlbumState.Base(storage.read()))
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(AlbumViewModel::class.java)
    }

    fun open(track: AudioUi, position: Int, mediaService: MediaService) {
        manageOrder.setActualTrack(position)
        //actualPosition = position
        val list = (storage.read() as AlbumUi.Base).tracks
        mediaService.open(list as List<AudioUi.Abstract>, track, position, false)
        navigation.update(PlayerScreen)

    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AlbumState>) {
        communication.observe(owner, observer)
    }
}