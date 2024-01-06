package com.maxim.musicplayer.player.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxim.musicplayer.cope.ProvideMediaService

class NotificationActionsBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service =
            (context!!.applicationContext as ProvideMediaService).mediaService()
        when (intent?.action) {
            "PLAY" -> service.play()
            "NEXT" -> service.next()
            "PREVIOUS" -> service.previous()
        }
    }
}