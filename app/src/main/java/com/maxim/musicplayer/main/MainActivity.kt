package com.maxim.musicplayer.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.AudioListViewModel
import com.maxim.musicplayer.cope.App
import com.maxim.musicplayer.cope.sl.ProvideViewModel
import com.maxim.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.setIcon(R.drawable.album_24)
                }

                1 -> {
                    tab.setIcon(R.drawable.baseline_audiotrack_24)
                }

                else -> {
                    tab.setIcon(R.drawable.favorite_24)
                }
            }
        }.attach()

        viewModel = viewModel(MainViewModel::class.java)
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container, binding.tabLayout)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == OPEN_PLAYER_ACTION) {
            viewModel.openPlayer()
        }
    }

    override fun onResume() { //todo refactor
        super.onResume()

        if (intent?.action == OPEN_PLAYER_ACTION) {
            viewModel.openPlayer()
        }

        if ((application as App).isMock) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val readResult = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
            val notificationResult = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (readResult != PackageManager.PERMISSION_GRANTED && notificationResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_MEDIA_AUDIO,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ),
                    200
                )
            } else if (readResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_MEDIA_AUDIO
                    ),
                    200
                )
            } else if (notificationResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ),
                    200
                )
            }
        } else {
            val readResult = ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (readResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    ),
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
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        "Please grand permissions",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
            viewModel(AudioListViewModel::class.java).init(true)
        }
    }

    override fun onStart() {
        super.onStart()
        (application as App).bind()
    }

    override fun <T : ViewModel> viewModel(clasz: Class<T>) =
        (application as ProvideViewModel).viewModel(clasz)

    companion object {
        const val OPEN_PLAYER_ACTION = "OPEN_PLAYER_ACTION"
    }
}