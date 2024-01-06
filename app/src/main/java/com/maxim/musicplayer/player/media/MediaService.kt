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
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.ProvideDownBarTrackCommunication
import com.maxim.musicplayer.cope.ProvideManageOrder
import com.maxim.musicplayer.cope.ProvidePlayerCommunication
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.player.presentation.PlayerCommunication
import com.maxim.musicplayer.player.presentation.PlayerState


interface MediaService : StartAudio, Playable {
    fun currentPosition(): Int
    fun seekTo(position: Int)
    fun setOnCompleteListener(action: () -> Unit)
    fun pause()
    fun open(list: List<AudioUi>, audio: AudioUi, position: Int)
    fun stop()
    fun isPlaying(): Boolean

    class Base : Service(), MediaService {
        private var mediaPlayer: MediaPlayer? = null
        private var actualUri: Uri? = null

        private val binder = MusicBinder()

        private var cachedTitle = ""
        private var cachedArtist = ""
        private var cachedIcon: Bitmap? = null

        private var isPlaying = false

        private lateinit var manageOrder: ManageOrder
        private lateinit var downBarTrackCommunication: DownBarTrackCommunication
        private lateinit var playerCommunication: PlayerCommunication

        inner class MusicBinder : Binder() {
            fun getService(): Base = this@Base
        }

        override fun onBind(intent: Intent?): IBinder {
            return binder
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
            manageOrder = (applicationContext as ProvideManageOrder).manageOrder()
            downBarTrackCommunication =
                (applicationContext as ProvideDownBarTrackCommunication).downBarTrackCommunication()
            playerCommunication =
                (applicationContext as ProvidePlayerCommunication).playerCommunication()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createChannel()
            mediaSessionCompat = MediaSessionCompat(this, "tag")
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            return START_STICKY
        }

        override fun start(
            title: String,
            artist: String,
            uri: Uri,
            icon: Bitmap?,
            ignoreSame: Boolean
        ) {

            startForeground(NOTIFICATION_ID, makeNotification(title, artist, icon, false))

            actualUri?.let {
                if (uri != actualUri || ignoreSame) {
                    mediaPlayer?.reset()
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this, uri)
                    mediaPlayer!!.setOnCompletionListener { next() }
                }
            }
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, uri)
                mediaPlayer!!.setOnCompletionListener { next() }
            }
            mediaPlayer!!.start()
            actualUri = uri

            cachedTitle = title
            cachedArtist = artist
            cachedIcon = icon
        }

        override fun play() {
            isPlaying = !isPlaying
            if (isPlaying) {
                val track = manageOrder.actualTrack()
                downBarTrackCommunication.setTrack(track, this)
                track.start(this)
                playerCommunication.update(PlayerState.Running)
            } else {
                downBarTrackCommunication.stop()
                playerCommunication.update(PlayerState.OnPause)
                pause()
            }
        }

        override fun next() {
            if (manageOrder.canGoNext()) {
                isPlaying = true
                val track = manageOrder.next()
                track.start(this)
                playerCommunication.update(
                    PlayerState.Initial(
                        track,
                        manageOrder.isRandom,
                        manageOrder.isLoop, false
                    )
                )
                manageOrder.setActualTrack(manageOrder.actualAbsolutePosition())
                downBarTrackCommunication.setTrack(track, this)
            }
        }


        override fun previous() {
            if (currentPosition() < TIME_TO_PREVIOUS_MAKE_RESTART && manageOrder.canGoPrevious()) {
                isPlaying = true
                val track = manageOrder.previous()
                track.start(this)
                manageOrder.setActualTrack(manageOrder.actualAbsolutePosition())
                downBarTrackCommunication.setTrack(track, this)
                playerCommunication.update(
                    PlayerState.Initial(
                        track,
                        manageOrder.isRandom,
                        manageOrder.isLoop, false
                    )
                )
            } else {
                val track = manageOrder.actualTrack()
                track.startAgain(this)
                playerCommunication.update(PlayerState.Running)
            }
        }

        override fun open(list: List<AudioUi>, audio: AudioUi, position: Int) {
            isPlaying = true
            manageOrder.generate(list, position)
            audio.start(this)
        }

        override fun stop() {
            Log.d("MyLog", "service close")
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        //todo if service will not die, remove try catch
        override fun isPlaying() = try {
            mediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            false
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
            Log.d("MyLog", "service onDestroy")
            super.onDestroy()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        }

        private lateinit var mediaSessionCompat: MediaSessionCompat

        private fun makeNotification(
            title: String,
            text: String,
            icon: Bitmap?,
            isPause: Boolean
        ): Notification {
            val intentPlay =
                Intent(applicationContext, NotificationActionsBroadcastReceiver::class.java).apply {
                    action = NotificationActionsBroadcastReceiver.PLAY_ACTION
                }
            val pendingIntentPlay = PendingIntent.getBroadcast(
                applicationContext, 0, intentPlay,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intentNext =
                Intent(applicationContext, NotificationActionsBroadcastReceiver::class.java).apply {
                    action = NotificationActionsBroadcastReceiver.NEXT_ACTION
                }
            val pendingIntentNext = PendingIntent.getBroadcast(
                applicationContext, 0, intentNext,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intentPrevious =
                Intent(applicationContext, NotificationActionsBroadcastReceiver::class.java).apply {
                    action = NotificationActionsBroadcastReceiver.PREVIOUS_ACTION
                }
            val pendingIntentPrevious = PendingIntent.getBroadcast(
                applicationContext, 0, intentPrevious,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intentStop =
                Intent(applicationContext, NotificationActionsBroadcastReceiver::class.java).apply {
                    action = NotificationActionsBroadcastReceiver.STOP_ACTION
                }
            val pendingIntentStop = PendingIntent.getBroadcast(
                applicationContext, 0, intentStop,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val largeIcon = if (icon != null) icon
            else {
                val drawable =
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_launcher_background)
                val bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.RGB_565)
                val canvas = Canvas(bitmap)
                drawable?.setBounds(0, 0, canvas.width, canvas.height)
                drawable?.draw(canvas)
                bitmap
            }

            return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(PRIORITY_MAX)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .addAction(R.drawable.previous_24, "Previous", pendingIntentPrevious)
                .addAction(
                    if (isPause) R.drawable.play_24 else R.drawable.pause_24,
                    "Play",
                    pendingIntentPlay
                )
                .addAction(R.drawable.next_24, "Next", pendingIntentNext)
                .addAction(R.drawable.close_24, "Stop", pendingIntentStop)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2, 4)
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
            private const val TIME_TO_PREVIOUS_MAKE_RESTART = 2500
        }
    }
}
