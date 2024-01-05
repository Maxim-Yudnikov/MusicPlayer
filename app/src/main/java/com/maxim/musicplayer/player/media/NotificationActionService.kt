package com.maxim.musicplayer.player.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.cope.ProvideViewModel
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class NotificationActionService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val viewModel =
            (context!!.applicationContext as ProvideViewModel).viewModel(PlayerViewModel::class.java)
        val mediaService = (context.applicationContext as ProvideMediaService).mediaService()
        when (intent?.action) {
            "PLAY" -> viewModel.play(mediaService)
            "NEXT" -> viewModel.next(mediaService)
            "PREVIOUS" -> viewModel.previous(mediaService)
        }
    }
}