package com.maxim.musicplayer.favoriteList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.presentation.AudioListCommunication
import com.maxim.musicplayer.audioList.presentation.AudioListState
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.presentation.BaseViewModel
import com.maxim.musicplayer.cope.presentation.Communication
import com.maxim.musicplayer.cope.presentation.Reload
import com.maxim.musicplayer.cope.presentation.RunAsync
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository

class FavoriteListViewModel(
    private val repository: FavoriteListRepository,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val audioListCommunication: AudioListCommunication,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<AudioListState>, Reload {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            repository.init(this)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        audioListCommunication.observe(owner, observer)
    }

    override fun reload() {
        val list = repository.data().map { it.map(mapper) }.toMutableList()
        list.add(0, AudioUi.Count(list.size))
        audioListCommunication.update(AudioListState.List(list, 0))
    }
}