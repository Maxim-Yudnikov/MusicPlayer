package com.maxim.musicplayer.player.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.content.ContextCompat
import com.maxim.musicplayer.R


interface MediaService : StartAudio {
    fun currentPosition(): Int
    fun seekTo(position: Int)
    fun setOnCompleteListener(action: () -> Unit)
    fun pause()

    class Base : Service(), MediaService {
        private var mediaPlayer: MediaPlayer? = null
        private var actualUri: Uri? = null

        private val binder = MusicBinder()

        private var cachedTitle = ""
        private var cachedArtist = ""
        private var cachedIcon: Bitmap? = null

        inner class MusicBinder : Binder() {
            fun getService(): Base = this@Base
        }

        override fun onBind(intent: Intent?): IBinder {
            return binder
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            return START_NOT_STICKY
        }

        override fun currentPosition() = mediaPlayer?.currentPosition ?: 0

        override fun seekTo(position: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                mediaPlayer!!.seekTo(position.toLong(), MediaPlayer.SEEK_CLOSEST)
            else
                mediaPlayer!!.seekTo(position)
        }

        override fun setOnCompleteListener(action: () -> Unit) {
            mediaPlayer!!.setOnCompletionListener {
                action.invoke()
            }
        }

        override fun onCreate() {
            super.onCreate()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createChannel()
        }

        override fun start(
            title: String,
            artist: String,
            uri: Uri,
            icon: Bitmap?,
            ignoreSame: Boolean
        ) {
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(
                NOTIFICATION_ID,
                makeNotification(title, artist, icon, false)
            )
            actualUri?.let {
                if (uri != actualUri || ignoreSame) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this, uri)
                }
            }
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(this, uri)
            mediaPlayer!!.start()
            actualUri = uri

            cachedTitle = title
            cachedArtist = artist
            cachedIcon = icon
        }

        override fun pause() {
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(
                NOTIFICATION_ID,
                makeNotification(cachedTitle, cachedArtist, cachedIcon, true)
            )
            mediaPlayer?.pause()
        }

        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer?.release()
        }

        private var mediaSessionCompat: MediaSessionCompat? = null

        private fun makeNotification(
            title: String,
            text: String,
            icon: Bitmap?,
            isPause: Boolean
        ): Notification {
            val intentPlay =
                Intent(applicationContext, NotificationActionService::class.java).apply {
                    action = "PLAY"
                }
            val pendingIntentPlay = PendingIntent.getBroadcast(
                applicationContext, 0, intentPlay,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intentNext =
                Intent(applicationContext, NotificationActionService::class.java).apply {
                    action = "NEXT"
                }
            val pendingIntentNext = PendingIntent.getBroadcast(
                applicationContext, 0, intentNext,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intentPrevious =
                Intent(applicationContext, NotificationActionService::class.java).apply {
                    action = "PREVIOUS"
                }
            val pendingIntentPrevious = PendingIntent.getBroadcast(
                applicationContext, 0, intentPrevious,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val largeIcon = if (icon != null) icon
            else {
                val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_launcher_background)
                val bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.RGB_565)
                val canvas = Canvas(bitmap)
                drawable?.setBounds(0, 0, canvas.width, canvas.height)
                drawable?.draw(canvas)
                bitmap
            }
            Log.d("MyLog", "$largeIcon")

            mediaSessionCompat?.release()
            mediaSessionCompat = MediaSessionCompat(this, "tag")
            return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(PRIORITY_MAX)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .addAction(R.drawable.previous_24, "PREVIOUS_BUTTON", pendingIntentPrevious)
                .addAction(
                    if (isPause) R.drawable.play_24 else R.drawable.pause_24,
                    "PLAY_BUTTON",
                    pendingIntentPlay
                )
                .addAction(R.drawable.next_24, "NEXT_BUTTON", pendingIntentNext)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat!!.sessionToken)
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
        }
    }
}