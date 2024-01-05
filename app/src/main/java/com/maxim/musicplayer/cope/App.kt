package com.maxim.musicplayer.cope

import android.app.Application
import androidx.lifecycle.ViewModel

class App : Application(), ProvideModule {
    private lateinit var factory: ModuleFactory
    override fun onCreate() {
        super.onCreate()
        factory = ModuleFactory.Base(ProvideModule.Base(Core(this)))
    }

    override fun <T : ViewModel> module(clasz: Class<T>): Module<T> {
        return factory.module(clasz)
    }
}