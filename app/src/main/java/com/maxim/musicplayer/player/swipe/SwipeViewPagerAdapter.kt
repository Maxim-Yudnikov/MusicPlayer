package com.maxim.musicplayer.player.swipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.maxim.musicplayer.audioList.presentation.AudioUi

class SwipeViewPagerAdapter(fragment: FragmentActivity, private val swipeState: SwipeState) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = swipeState.getItemCount()

    override fun createFragment(position: Int) = swipeState.createFragment(position)
}

interface SwipeState {
    fun getItemCount(): Int
    fun createFragment(position: Int): Fragment
    fun setCurrentItem(viewPager2: ViewPager2)

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

        override fun setCurrentItem(viewPager2: ViewPager2) {
            viewPager2.setCurrentItem(1, false)
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

        override fun setCurrentItem(viewPager2: ViewPager2) {
            viewPager2.setCurrentItem(0, false)
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

        override fun setCurrentItem(viewPager2: ViewPager2) {
            viewPager2.setCurrentItem(1, false)
        }
    }

    class Single(
        private val current: AudioUi
    ) : SwipeState {
        override fun getItemCount() = 1

        override fun createFragment(position: Int) = SwipeFragment.newInstance(current)

        override fun setCurrentItem(viewPager2: ViewPager2) {
            viewPager2.setCurrentItem(0, false)
        }
    }
}