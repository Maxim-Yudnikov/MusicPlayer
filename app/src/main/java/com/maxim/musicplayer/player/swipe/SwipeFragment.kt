package com.maxim.musicplayer.player.swipe

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxim.musicplayer.R
import com.maxim.musicplayer.audioList.presentation.ArtImageView
import com.maxim.musicplayer.audioList.presentation.AudioUi

class SwipeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.player_art_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val art = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getParcelable(ART_URI, Uri::class.java)!!
        else requireArguments().getParcelable(ART_URI)!!

        view.findViewById<ArtImageView>(R.id.artImageView).setArt(art, true)
    }

    companion object {
        fun newInstance(audioUi: AudioUi): SwipeFragment {
            return SwipeFragment().apply {
                arguments = Bundle().apply {
                    audioUi.putArt(ART_URI, this)
                }
            }
        }

        private const val ART_URI = "ART_URI"
    }
}