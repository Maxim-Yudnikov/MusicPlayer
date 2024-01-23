package com.maxim.musicplayer.player.swipe

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.media.MediaService

interface SwipeState {
    fun getItemCount(): Int
    fun createFragment(position: Int): Fragment
    fun setCurrentItem(viewPager: ViewPager2)
    fun swipeListener(mediaService: MediaService): ViewPager2.OnPageChangeCallback
    fun show(swipeAdapter: SwipeAdapter)

    class All(
        private val previous: AudioUi,
        private val current: AudioUi,
        private val next: AudioUi
    ) : SwipeState {
        override fun getItemCount() = 3

        override fun createFragment(position: Int) =
            when (position) {
                0 -> SwipeFragment.newInstance(previous)
                1 -> SwipeFragment.newInstance(current)
                else -> SwipeFragment.newInstance(next)
            }

        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(1, false)
        }

        override fun swipeListener(mediaService: MediaService): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> mediaService.previous()
                        2 -> mediaService.next()
                    }
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
        override fun getItemCount() = 2

        override fun createFragment(position: Int) =
            when (position) {
                0 -> SwipeFragment.newInstance(current)
                else -> SwipeFragment.newInstance(next)
            }

        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(0, false)
        }

        override fun swipeListener(mediaService: MediaService): ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 1)
                        mediaService.next()
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
        override fun getItemCount() = 2

        override fun createFragment(position: Int) =
            when (position) {
                0 -> SwipeFragment.newInstance(previous)
                else -> SwipeFragment.newInstance(current)
            }

        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(1, false)
        }

        override fun swipeListener(mediaService: MediaService) =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0)
                        mediaService.previous()
                }
            }

        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(previous, current))
        }
    }

    class Single(
        private val current: AudioUi
    ) : SwipeState {
        override fun getItemCount() = 1

        override fun createFragment(position: Int) = SwipeFragment.newInstance(current)

        override fun setCurrentItem(viewPager: ViewPager2) {
            viewPager.setCurrentItem(0, false)
        }

        override fun swipeListener(mediaService: MediaService) =
            object : ViewPager2.OnPageChangeCallback() {}

        override fun show(swipeAdapter: SwipeAdapter) {
            swipeAdapter.update(listOf(current))
        }
    }
}