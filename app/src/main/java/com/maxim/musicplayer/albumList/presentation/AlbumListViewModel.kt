package com.maxim.musicplayer.albumList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.albumList.data.AlbumDomain
import com.maxim.musicplayer.albumList.data.AlbumListRepository
import com.maxim.musicplayer.core.presentation.Communication

class AlbumListViewModel(
    private val communication: AlbumListCommunication,
    private val repository: AlbumListRepository,
    private val mapper: AlbumDomain.Mapper<AlbumUi>
): ViewModel(), Communication.Observe<AlbumListState> {

    fun init() {
        communication.update(AlbumListState.Base(repository.data().map { it.map(mapper) }))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AlbumListState>) {
        communication.observe(owner, observer)
    }
}