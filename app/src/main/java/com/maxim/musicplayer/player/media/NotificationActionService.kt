package com.maxim.musicplayer.player.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.cope.ProvideViewModel
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class NotificationActionService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val viewModel =
            (context!!.applicationContext as ProvideViewModel).viewModel(PlayerViewModel::class.java)
        when (intent?.action) {
            "PLAY" -> viewModel.play((context.applicationContext as ProvideMediaService).mediaService())
            "NEXT" -> viewModel.next()
            "PREVIOUS" -> viewModel.previous()
        }
        Log.d("MyLog", intent?.action ?: "empty intent")
    }
}