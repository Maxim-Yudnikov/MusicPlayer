package com.maxim.musicplayer.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.media.MediaService.Base.Companion.NEXT_ACTION
import com.maxim.musicplayer.media.MediaService.Base.Companion.PLAY_ACTION
import com.maxim.musicplayer.media.MediaService.Base.Companion.PREVIOUS_ACTION
import com.maxim.musicplayer.media.MediaService.Base.Companion.STOP_ACTION

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
}