package com.maxim.musicplayer.player.swipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maxim.musicplayer.player.swipe.left.LeftSwipeFragment
import com.maxim.musicplayer.player.swipe.middle.MiddleSwipeFragment
import com.maxim.musicplayer.player.swipe.right.RightSwipeFragment

class SwipeViewPagerAdapter(fragment: FragmentActivity, private val swipeState: SwipeState) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = swipeState.getItemCount()

    override fun createFragment(position: Int) = swipeState.createFragment(position)
}

interface SwipeState {
    fun getItemCount(): Int
    fun createFragment(position: Int): Fragment

    class All : SwipeState {
        override fun getItemCount() = 3

        override fun createFragment(position: Int) =
            when (position) {
                0 -> LeftSwipeFragment()
                1 -> MiddleSwipeFragment()
                else -> RightSwipeFragment()
            }
    }

    class Start: SwipeState {
        override fun getItemCount() = 2

        override fun createFragment(position: Int) =
            when (position) {
                0 -> MiddleSwipeFragment()
                else -> RightSwipeFragment()
            }
    }

    class End: SwipeState {
        override fun getItemCount() = 2

        override fun createFragment(position: Int) =
            when (position) {
                0 -> LeftSwipeFragment()
                else -> MiddleSwipeFragment()
            }
    }

    class Single: SwipeState {
        override fun getItemCount() = 1

        override fun createFragment(position: Int) = MiddleSwipeFragment()
    }
}