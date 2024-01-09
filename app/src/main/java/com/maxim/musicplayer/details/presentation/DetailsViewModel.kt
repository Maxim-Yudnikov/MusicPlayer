package com.maxim.musicplayer.details.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Init
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack

class DetailsViewModel(
    private val communication: DetailsCommunication,
    private val storage: DetailsStorage.Read,
    private val clear: ClearViewModel
) : ViewModel(), Communication.Observe<DetailsState>, Init, GoBack {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {

        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DetailsState>) {
        communication.observe(owner, observer)
    }

    override fun goBack() {
        clear.clear(DetailsViewModel::class.java)
    }
}