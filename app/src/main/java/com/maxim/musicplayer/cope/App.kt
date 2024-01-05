package com.maxim.musicplayer.cope

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.player.media.MediaService

class App : Application(), ProvideViewModel, ProvideMediaService, ManageOrder {
    private lateinit var factory: ModuleFactory
    override fun onCreate() {
        super.onCreate()
        factory = ModuleFactory.Base(ProvideModule.Base(Core(this)))

        val intent = Intent(this, MediaService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private var isBound = false
    private var mediaService: MediaService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaService.MusicBinder
            mediaService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun mediaService() = mediaService!!
    override fun <T : ViewModel> viewModel(clasz: Class<T>) = factory.viewModel(clasz)

    private val orderList = mutableListOf<AudioUi>()
    private var actualPosition = 0
    override fun generate(tracks: List<AudioUi>, position: Int) {
        orderList.clear()
        orderList.addAll(tracks)
        actualPosition = position
    }

    override fun next(): AudioUi {
        if (actualPosition == orderList.lastIndex)
            return orderList[actualPosition]
        return orderList[++actualPosition]
    }

    override fun previous(): AudioUi {
        if (actualPosition == 0)
            return orderList[actualPosition]
        return orderList[--actualPosition]
    }
}

interface ProvideMediaService {
    fun mediaService(): MediaService
}

interface ManageOrder {
    fun generate(tracks: List<AudioUi>, position: Int)
    fun next(): AudioUi
    fun previous(): AudioUi
}