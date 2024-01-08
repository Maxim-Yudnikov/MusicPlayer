package com.maxim.musicplayer.player

import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.favoriteList.data.FavoriteListRepository
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class PlayerModule(
    private val core: Core,
    private val provideMediaService: ProvideMediaService,
    private val clearViewModel: ClearViewModel
) : Module<PlayerViewModel> {
    override fun viewModel() = PlayerViewModel(
        core.downBarTrackCommunication(),
        core.playerCommunication(),
        core.manageOrder(),
        provideMediaService,
        core.navigation(),
        clearViewModel,
        FavoriteListRepository.Base(core.database().dao())
    )
}