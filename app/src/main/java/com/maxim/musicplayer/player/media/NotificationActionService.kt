package com.maxim.musicplayer.player.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.maxim.musicplayer.cope.ProvideModule
import com.maxim.musicplayer.player.presentation.PlayerViewModel

class NotificationActionService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val viewModel =
            (context!!.applicationContext as ProvideModule).module(PlayerViewModel::class.java)
                .viewModel()
        when (intent?.action) {
            //"PLAY" -> viewModel.play()
            "NEXT" -> viewModel.next()
            "PREVIOUS" -> viewModel.previous()
        }
        Log.d("MyLog", intent?.action ?: "empty intent")
    }
}