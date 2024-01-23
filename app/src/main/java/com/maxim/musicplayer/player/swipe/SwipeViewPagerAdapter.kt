package com.maxim.musicplayer.player.swipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.media.MediaService

class SwipeViewPagerAdapter(fragment: FragmentActivity, private val swipeState: SwipeState) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = swipeState.getItemCount()

    override fun createFragment(position: Int) = swipeState.createFragment(position)
}

interface SwipeState {
    fun getItemCount(): Int
    fun createFragment(position: Int): Fragment
    fun setCurrentItem(viewPager: ViewPager2)
    fun swipeListener(swipeViewPager: ViewPager2, mediaService: MediaService): OnPageChangeCallback

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

        override fun swipeListener(swipeViewPager: ViewPager2, mediaService: MediaService) =
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    swipeViewPager.unregisterOnPageChangeCallback(this)
                    when (position) {
                        0 -> mediaService.previous()
                        2 -> mediaService.next()
                    }
                }
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

        override fun swipeListener(swipeViewPager: ViewPager2, mediaService: MediaService) =
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    swipeViewPager.unregisterOnPageChangeCallback(this)
                    mediaService.next()
                }
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

        override fun swipeListener(swipeViewPager: ViewPager2, mediaService: MediaService) =
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    swipeViewPager.unregisterOnPageChangeCallback(this)
                    mediaService.previous()
                }
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

        override fun swipeListener(swipeViewPager: ViewPager2, mediaService: MediaService) =
            object : OnPageChangeCallback() {}
    }
}