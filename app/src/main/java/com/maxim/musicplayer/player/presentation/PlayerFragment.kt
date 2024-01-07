package com.maxim.musicplayer.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.maxim.musicplayer.cope.BaseFragment
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.databinding.FragmentPlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerFragment : BaseFragment<FragmentPlayerBinding, PlayerViewModel>() {
    override fun viewModelClass() = PlayerViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(inflater, container, false)

    private var coroutineIsRunning = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(
                binding.artImageView,
                binding.titleTextView,
                binding.artistTextView,
                binding.playButton,
                binding.randomOrderButton,
                binding.loopOrderButton,
                binding.seekBar,
                binding.actualTimeTextView,
                binding.durationTextView
            )
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

        val mediaService = (requireContext().applicationContext as ProvideMediaService).mediaService()
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaService.seekTo(progress)
                    binding.actualTimeTextView.text =
                        getTime(mediaService.currentPosition() / 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        CoroutineScope(Dispatchers.Default).launch {
            delay(1300)
            while (coroutineIsRunning) {
                withContext(Dispatchers.Main) {
                    if (mediaService.isPlaying()) {
                        val currentPosition = mediaService.currentPosition()
                        binding.seekBar.progress = currentPosition
                        binding.actualTimeTextView.text = getTime(currentPosition / 1000)
                    }
                }
                delay(1000)
            }
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onDestroyView() {
        coroutineIsRunning = false
        super.onDestroyView()
    }

    //todo private function and the same in audioUi and state
    private fun getTime(seconds: Int): String {
        val minutes = seconds / 60
        val second = seconds % 60
        return "$minutes:${if (second < 10) "0$second" else second}"
    }
}