package com.maxim.musicplayer.cope

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.downBar.DownBarTrackCommunication
import com.maxim.musicplayer.player.media.ManageOrder
import com.maxim.musicplayer.player.media.MediaService
import com.maxim.musicplayer.player.presentation.PlayerCommunication

class App : Application(), ProvideViewModel, ProvideMediaService, ProvideManageOrder,
    ProvideDownBarTrackCommunication, ProvidePlayerCommunication {
    private lateinit var factory: ModuleFactory
    private lateinit var manageOrder: ManageOrder

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        factory = ModuleFactory.Base(ProvideModule.Base(Core(this)))
        manageOrder =
            ManageOrder.Base(SimpleStorage.Base(getSharedPreferences(STORAGE_NAME, MODE_PRIVATE)))
        startService(Intent(this, MediaService::class.java))
    }

    private var isBound = false
    private var mediaService: MediaService.Base? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaService.Base.MusicBinder
            mediaService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    fun bind() {
        bindService(
            Intent(this, MediaService.Base::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

//    fun unbind() {
//        if (isBound) {
//            unbindService(connection)
//            isBound = false
//        }
//    }

    override fun mediaService() = mediaService!!
    override fun manageOrder(): ManageOrder = manageOrder
    override fun <T : ViewModel> viewModel(clasz: Class<T>) = factory.viewModel(clasz)

    companion object {
        private const val STORAGE_NAME = "MUSIC_PLAYER_STORAGE"
    }

    private val downBarTrackCommunication = DownBarTrackCommunication.Base()
    override fun downBarTrackCommunication() = downBarTrackCommunication
    private val playerCommunication = PlayerCommunication.Base()
    override fun playerCommunication() = playerCommunication
}

interface ProvideMediaService {
    fun mediaService(): MediaService
}

interface ProvideManageOrder {
    fun manageOrder(): ManageOrder
}

interface ProvideDownBarTrackCommunication {
    fun downBarTrackCommunication(): DownBarTrackCommunication
}

interface ProvidePlayerCommunication {
    fun playerCommunication(): PlayerCommunication
}