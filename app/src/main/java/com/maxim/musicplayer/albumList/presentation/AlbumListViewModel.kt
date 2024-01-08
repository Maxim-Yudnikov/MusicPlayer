package com.maxim.musicplayer.albumList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.albumList.data.AlbumDomain
import com.maxim.musicplayer.albumList.data.AlbumListRepository
import com.maxim.musicplayer.audioList.presentation.RefreshFinish
import com.maxim.musicplayer.core.presentation.BaseViewModel
import com.maxim.musicplayer.core.presentation.Communication

class AlbumListViewModel(
    private val communication: AlbumListCommunication,
    private val repository: AlbumListRepository,
    private val mapper: AlbumDomain.Mapper<AlbumUi>
) : BaseViewModel(), Communication.Observe<AlbumListState> {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(AlbumListState.Base(repository.data().map { it.map(mapper) }))
    }

    fun refresh(refreshFinish: RefreshFinish) {
        handle({repository.data()}) { list ->
            communication.update(AlbumListState.Base(list.map { it.map(mapper) }))
            refreshFinish.finish()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AlbumListState>) {
        communication.observe(owner, observer)
    }
}