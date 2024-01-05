package com.maxim.musicplayer.player.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.maxim.musicplayer.R

class MediaService : Service(), StartAudio {
    private var mediaPlayer: MediaPlayer? = null
    private var actualUri: Uri? = null

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MediaService = this@MediaService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun start(title: String, artist: String, uri: Uri) {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel()
        notificationManager.notify(
            NOTIFICATION_ID,
            makeNotification(title, artist)
        )
        actualUri?.let {
            if (uri != actualUri) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this, uri)
            }
        }
        if (mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer!!.start()
        mediaPlayer!!.isLooping = true
        actualUri = uri
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun makeNotification(title: String, text: String): Notification {
        val intentPlay = Intent(applicationContext, NotificationActionService::class.java).apply {
            action = "PLAY"
        }
        val pendingIntentPlay = PendingIntent.getBroadcast(
            applicationContext, 0, intentPlay,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intentNext = Intent(applicationContext, NotificationActionService::class.java).apply {
            action = "NEXT"
        }
        val pendingIntentNext = PendingIntent.getBroadcast(
            applicationContext, 0, intentNext,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intentPrevious = Intent(applicationContext, NotificationActionService::class.java).apply {
            action = "PREVIOUS"
        }
        val pendingIntentPrevious = PendingIntent.getBroadcast(
            applicationContext, 0, intentPrevious,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val mediaSessionCompat = MediaSessionCompat(this, "tag")
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(PRIORITY_MAX)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .addAction(R.drawable.previous_24, "PREVIOUS_BUTTON", pendingIntentPrevious)
            .addAction(R.drawable.play_24, "PLAY_BUTTON", pendingIntentPlay)
            .addAction(R.drawable.next_24, "NEXT_BUTTON", pendingIntentNext)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSessionCompat.sessionToken)
            )
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, "Pomodoro", NotificationManager.IMPORTANCE_LOW)
        channel.description = "Pomodoro time"
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 123456789
        private const val CHANNEL_ID = "Player"
        private const val ACTION_PLAY = "ACTION_PLAY"
    }
}