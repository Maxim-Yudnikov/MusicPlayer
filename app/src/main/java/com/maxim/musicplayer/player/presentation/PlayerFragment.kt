package com.maxim.musicplayer.player.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.maxim.musicplayer.cope.BaseFragment
import com.maxim.musicplayer.cope.ProvideMediaService
import com.maxim.musicplayer.databinding.FragmentPlayerBinding
import com.maxim.musicplayer.player.media.MediaService

class PlayerFragment : BaseFragment<FragmentPlayerBinding, PlayerViewModel>() {
    private lateinit var runnable: Runnable
    private val handler = Handler()
    private lateinit var mediaService: MediaService
    override fun viewModelClass() = PlayerViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(
                binding.artImageView,
                binding.titleTextView,
                binding.artistTextView,
                binding.playButton,
                binding.seekBar,
                binding.actualTimeTextView,
                binding.durationTextView
            )
        }
        mediaService =
            (requireContext().applicationContext as ProvideMediaService).mediaService()

        binding.playButton.setOnClickListener {
            viewModel.play(mediaService)
        }

        binding.nextButton.setOnClickListener {
            viewModel.next(mediaService)
        }

        binding.previousButton.setOnClickListener {
            viewModel.previous(mediaService)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaService.seekTo(progress)
                    binding.actualTimeTextView.text = getTime(mediaService.currentPosition() / 1000 + 1)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        runnable = Runnable {
            val currentPosition = mediaService.currentPosition()
            Log.d("MyLog", "currentPos: $currentPosition")
            binding.seekBar.progress = currentPosition
            binding.actualTimeTextView.text = getTime(currentPosition / 1000)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1300)

        viewModel.init(savedInstanceState == null, mediaService)


    }

    override fun onResume() {
        super.onResume()
        val currentPosition = mediaService.currentPosition()
        binding.seekBar.progress = currentPosition
        binding.actualTimeTextView.text = getTime(currentPosition / 1000)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    //todo private function and the same in audioUi
    private fun getTime(seconds: Int): String {
        val minutes = seconds / 60
        val second = seconds % 60
        return "$minutes:${if (second < 10) "0$second" else second}"
    }
}