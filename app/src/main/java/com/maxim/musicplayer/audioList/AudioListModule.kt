package com.maxim.musicplayer.audioList

import com.maxim.musicplayer.audioList.data.AudioListRepository
import com.maxim.musicplayer.audioList.data.MapperDataToDomain
import com.maxim.musicplayer.audioList.domain.AudioListInteractor
import com.maxim.musicplayer.audioList.domain.MapperDomainToUi
import com.maxim.musicplayer.audioList.presentation.AudioListCommunication
import com.maxim.musicplayer.audioList.presentation.AudioListViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module

class AudioListModule(private val core: Core) : Module<AudioListViewModel> {
    override fun viewModel() = AudioListViewModel(
        AudioListInteractor.Base(
            AudioListRepository.Base(
                core.tracksCacheDataSource(),
                MapperDataToDomain(),
            ),
            core.favoriteRepository(),
        ),
        AudioListCommunication.Base(),
        MapperDomainToUi(),
        core.navigation(),
        core.manageOrder(),
        core.moreStorage(),
        core.favoriteRepository()
    )
}