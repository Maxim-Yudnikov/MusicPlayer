package com.maxim.musicplayer.details

import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module
import com.maxim.musicplayer.details.data.DetailsRepository
import com.maxim.musicplayer.details.presentation.DetailsCommunication
import com.maxim.musicplayer.details.presentation.DetailsViewModel

class DetailsModule(private val core: Core, private val clear: ClearViewModel): Module<DetailsViewModel> {
    override fun viewModel() = DetailsViewModel(
        DetailsCommunication.Base(),
        core.detailsStorage(),
        DetailsRepository.Base(core.tracksProvider()),
        clear
    )
}