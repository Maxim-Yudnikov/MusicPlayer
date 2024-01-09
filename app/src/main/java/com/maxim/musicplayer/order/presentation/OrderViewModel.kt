package com.maxim.musicplayer.order.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.core.presentation.Communication
import com.maxim.musicplayer.core.presentation.Init
import com.maxim.musicplayer.core.presentation.Navigation
import com.maxim.musicplayer.core.presentation.Reload
import com.maxim.musicplayer.core.presentation.Screen
import com.maxim.musicplayer.core.sl.ClearViewModel
import com.maxim.musicplayer.core.sl.GoBack
import com.maxim.musicplayer.player.media.ManageOrder

class OrderViewModel(
    private val communication: OrderCommunication,
    private val manageOrder: ManageOrder,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
) : ViewModel(), GoBack, Communication.Observe<OrderState>, Init, Reload {

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            reload()
            manageOrder.observeAnyPosition(this)
        }
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(OrderViewModel::class.java)
    }

    fun remove(id: Long) {
        manageOrder.removeTrackFromActualOrder(id)
        reload()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<OrderState>) {
        communication.observe(owner, observer)
    }

    override fun reload() {
        val list = ArrayList<AudioUi>(manageOrder.actualOrder())
        var duration = 0L
        list.forEach {
            duration += it.duration()
        }
        list.add(0, AudioUi.OrderTitle(manageOrder.actualPosition() + 1, list.size, duration.toInt()))
        communication.update(OrderState.Base(list, manageOrder.actualPosition()))
    }
}