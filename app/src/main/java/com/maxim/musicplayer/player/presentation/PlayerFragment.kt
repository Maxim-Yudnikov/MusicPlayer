package com.maxim.musicplayer.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.maxim.musicplayer.core.ProvideMediaService
import com.maxim.musicplayer.core.presentation.BaseFragment
import com.maxim.musicplayer.databinding.FragmentPlayerBinding
import com.maxim.musicplayer.media.MediaService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerFragment : BaseFragment<FragmentPlayerBinding, PlayerViewModel>(),
    SetViewPagerListener {
    override fun viewModelClass() = PlayerViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(inflater, container, false)

    private lateinit var mediaService: MediaService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(
                binding.trackViewPager!!, //todo
                this,
                binding.playerTitleTextView,
                binding.artistTextView,
                binding.playButton,
                binding.randomOrderButton,
                binding.loopOrderButton,
                binding.seekBar,
                binding.actualTimeTextView,
                binding.durationTextView,
                binding.favoriteButton
            )
            it.finish(viewModel)
        }

        binding.favoriteButton.setOnClickListener {
            binding.favoriteButton.isEnabled = false
            viewModel.saveToFavorites()
        }

        binding.playButton.setOnClickListener {
            viewModel.play()
        }

        binding.nextButton.setOnClickListener {
            viewModel.next()
        }

        binding.previousButton.setOnClickListener {
            viewModel.previous()
        }

        binding.loopOrderButton.setOnClickListener {
            viewModel.changeLoop()
        }

        binding.randomOrderButton.setOnClickListener {
            viewModel.changeRandom()
        }

        binding.orderButton.setOnClickListener {
            viewModel.order()
        }

        mediaService = (requireContext().applicationContext as ProvideMediaService).mediaService()
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaService.seekTo(progress)
                    binding.actualTimeTextView.showTime(mediaService.currentPosition() / 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        viewModel.init(savedInstanceState == null)
    }

    private var job: Job? = null

    override fun onStart() {
        super.onStart()
        job = CoroutineScope(Dispatchers.Default).launch {
            delay(1300)
            while (true) {
                withContext(Dispatchers.Main) {
                    if (mediaService.isPlaying()) {
                        val currentPosition = mediaService.currentPosition()
                        binding.seekBar.progress = currentPosition
                        binding.actualTimeTextView.showTime(currentPosition / 1000)
                    }
                }
                delay(1000)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        job = null
    }

    private var cachedCallback: OnPageChangeCallback? = null
    override fun set(callback: OnPageChangeCallback) {
        cachedCallback = callback
        binding.trackViewPager!!.registerOnPageChangeCallback(cachedCallback!!)
    }

    override fun remove() {
        cachedCallback?.let {
            binding.trackViewPager!!.unregisterOnPageChangeCallback(it)
        }
    }
}

interface SetViewPagerListener {
    fun set(callback: OnPageChangeCallback)
    fun remove()
}