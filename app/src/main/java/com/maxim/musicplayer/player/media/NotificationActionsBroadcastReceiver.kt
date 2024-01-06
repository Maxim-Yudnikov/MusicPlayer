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
            PLAY_ACTION -> service.play()
            NEXT_ACTION -> service.next()
            PREVIOUS_ACTION -> service.previous()
            STOP_ACTION -> service.stop()
        }
    }

    companion object {
        const val PLAY_ACTION = "PLAY"
        const val NEXT_ACTION = "NEXT"
        const val PREVIOUS_ACTION = "PREVIOUS"
        const val STOP_ACTION = "STOP"
    }
}