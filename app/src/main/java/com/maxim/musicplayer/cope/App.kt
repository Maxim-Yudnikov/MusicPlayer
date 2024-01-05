package com.maxim.musicplayer.cope

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.player.media.MediaService

class App : Application(), ProvideViewModel, ProvideMediaService {
    private lateinit var factory: ModuleFactory
    override fun onCreate() {
        super.onCreate()
        factory = ModuleFactory.Base(ProvideModule.Base(Core(this)))

        val intent = Intent(this, MediaService.Base::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
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

    override fun mediaService() = mediaService!!
    override fun <T : ViewModel> viewModel(clasz: Class<T>) = factory.viewModel(clasz)
}

interface ProvideMediaService {
    fun mediaService(): MediaService
}