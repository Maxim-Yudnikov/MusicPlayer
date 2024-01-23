package com.maxim.musicplayer.player.swipe

import androidx.viewpager2.widget.ViewPager2
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.media.MediaService

interface SwipeState {
    fun setCurrentItem(viewPager: ViewPager2)
    fun swipeListener(viewPager: ViewPager2, mediaService: MediaService): ViewPager2.OnPageChangeCallback
    fun show(swipeAdapter: SwipeAdapter)

    class All(
        private val previous: AudioUi,
        private val current: AudioUi,
        private val next: AudioUi
    ) : SwipeState {
        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(1, false)
        }

        override fun swipeListener(
            viewPager: ViewPager2,
            mediaService: MediaService
        ): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(false)
                    when (position) {
                        0 -> mediaService.previous()
                        2 -> mediaService.next()
                    }
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(true)
                }
            }

        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(previous, current, next))
        }
    }

    class Start(
        private val current: AudioUi,
        private val next: AudioUi
    ) : SwipeState {
        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(0, false)
        }

        override fun swipeListener(
            viewPager: ViewPager2,
            mediaService: MediaService
        ): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(false)
                    if (position == 1)
                        mediaService.next()
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(true)
                }
            }
        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(current, next))
        }
    }

    class End(
        private val previous: AudioUi,
        private val current: AudioUi
    ) : SwipeState {
        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(1, false)
        }

        override fun swipeListener(
            viewPager: ViewPager2,
            mediaService: MediaService
        ): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(false)
                    if (position == 0)
                        mediaService.previous()
                    (viewPager.adapter as SwipeAdapter).setUpdateAll(true)
                }
            }

        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(previous, current))
        }
    }

    class Single(
        private val current: AudioUi
    ) : SwipeState {
        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(0, false)
        }

        override fun swipeListener(
            viewPager: ViewPager2,
            mediaService: MediaService
        ): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {}

        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(current))
        }
    }
}