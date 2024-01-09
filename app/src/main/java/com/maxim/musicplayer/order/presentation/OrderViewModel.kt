package com.maxim.musicplayer.order.presentation

import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Screen
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack

class OrderViewModel(
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
): ViewModel(), GoBack {

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(OrderViewModel::class.java)
    }
}