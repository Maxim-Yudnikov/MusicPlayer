package com.maxim.musicplayer.order

import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.Core
import com.maxim.musicplayer.core.sl.Module
import com.maxim.musicplayer.order.presentation.OrderViewModel

class OrderModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<OrderViewModel> {
    override fun viewModel() = OrderViewModel(
        core.navigation(),
        clearViewModel
    )
}