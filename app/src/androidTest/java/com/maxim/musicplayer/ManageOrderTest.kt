package com.maxim.musicplayer

import org.junit.Test

class ManageOrderTest: BaseTest() {

    @Test
    fun test_delete_track() {
        openTrackFromRecyclerview(2)
        manageOrder()
        removeFromCurrentOrder(3)
        back()
        checkTextInPlayer("Title 2")
        nextButton()
        checkTextInPlayer("Title 4")
    }

    @Test
    fun test_delete_track_in_random_order() {
        openTrackFromRecyclerview(4)
        randomButton()
        manageOrder()
        removeFromCurrentOrder(2)
        back()
        checkTextInPlayer("Title 4")
        nextButton()
        checkTextInPlayer("Title 3")
        nextButton()
        checkTextInPlayer("Title 5")
    }

    @Test
    fun test_delete_track_in_random_order_and_make_normal_order() {
        openTrackFromRecyclerview(4)
        randomButton()
        manageOrder()
        removeFromCurrentOrder(2)
        back()
        checkTextInPlayer("Title 4")
        randomButton()
        previousButton()
        checkTextInPlayer("Title 3")
        previousButton()
        checkTextInPlayer("Title 2")
        previousButton()
        checkTextInPlayer("Title 2")
    }
}