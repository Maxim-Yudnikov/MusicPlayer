package com.maxim.musicplayer.trackMore

import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module
import com.maxim.musicplayer.trackMore.presentation.MoreCommunication
import com.maxim.musicplayer.trackMore.presentation.MoreViewModel

class MoreModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<MoreViewModel> {
    override fun viewModel() = MoreViewModel(
        MoreCommunication.Base(),
        core.moreStorage(),
        core.manageOrder(),
        core.provideMediaService(),
        core.favoriteRepository(),
        core.detailsStorage(),
        core.navigation(),
        clearViewModel
    )
}