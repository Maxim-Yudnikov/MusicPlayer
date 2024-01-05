package com.maxim.musicplayer.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.maxim.musicplayer.R
import com.maxim.musicplayer.cope.ProvideModule
import com.maxim.musicplayer.player.media.MediaService

class MainActivity : AppCompatActivity(), ProvideModule, ProvideMediaService {
    private lateinit var viewModel: MainViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = module(MainViewModel::class.java).viewModel()
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MediaService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                    200
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "Please grand permission for read storage",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else
                viewModel.init(true)
        }
    }

    override fun <T : ViewModel> module(clasz: Class<T>) =
        (application as ProvideModule).module(clasz)

    override fun mediaService() = mediaService!!
}

interface ProvideMediaService {
    fun mediaService(): MediaService
}