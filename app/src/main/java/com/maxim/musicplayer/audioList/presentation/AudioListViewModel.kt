package com.maxim.musicplayer.audioList.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.domain.AudioDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.cope.Communication
import com.maxim.musicplayer.cope.Init
import com.maxim.musicplayer.cope.Navigation
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.presentation.OpenPlayerStorage
import com.maxim.musicplayer.player.presentation.PlayerScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioListViewModel(
    private val interactor: AudioListInteractor,
    private val communication: AudioListCommunication,
    private val mapper: AudioDomain.Mapper<AudioUi>,
    private val sharedStorage: OpenPlayerStorage.Save,
    private val navigation: Navigation.Update,
    private val manageOrder: ManageOrder
) : ViewModel(), Init, Communication.Observe<AudioListState> {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun init(isFirstRun: Boolean) {
        communication.update(AudioListState.List(interactor.data().map { it.map(mapper) }))
        viewModelScope.launch(Dispatchers.IO) {
            val state = AudioListState.List(interactor.dataWithImages().map { it.map(mapper) })
            withContext(Dispatchers.Main){
                communication.update(state)
            }
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AudioListState>) {
        communication.observe(owner, observer)
    }

    fun open(audio: AudioUi, position: Int) {
        sharedStorage.save(audio)
        manageOrder.generate(interactor.cachedData().map { it.map(mapper) }, position)
        navigation.update(PlayerScreen)
    }
}