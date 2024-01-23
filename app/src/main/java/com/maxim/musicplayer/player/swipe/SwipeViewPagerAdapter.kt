package com.maxim.musicplayer.player.swipe
//
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//
//class SwipeViewPagerAdapter(fragment: FragmentActivity) :
//    FragmentStateAdapter(fragment) {
//    private var currentState: SwipeState = SwipeState.Empty
//
//    override fun getItemCount() = currentState.getItemCount()
//
//    override fun createFragment(position: Int) = currentState.createFragment(position)
//
//    fun showState(swipeState: SwipeState) {
//        currentState = swipeState
//        notifyDataSetChanged()
//    }
//}