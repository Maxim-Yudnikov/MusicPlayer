package com.maxim.musicplayer.favoriteList

import com.maxim.musicplayer.audioList.domain.MapperDomainToUi
import com.maxim.musicplayer.audioList.presentation.AudioListCommunication
import com.maxim.musicplayer.cope.sl.Core
import com.maxim.musicplayer.cope.sl.Module
import com.maxim.musicplayer.favoriteList.presentation.FavoriteListViewModel

class FavoriteModule(private val core: Core): Module<FavoriteListViewModel> {
    override fun viewModel() = FavoriteListViewModel(
        core.favoriteRepository(),
        MapperDomainToUi(),
        AudioListCommunication.Base(),
        core.manageOrder(),
        core.navigation(),
        core.moreStorage()
    )
}