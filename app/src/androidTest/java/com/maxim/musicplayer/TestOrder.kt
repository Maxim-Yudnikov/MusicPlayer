package com.maxim.musicplayer

import org.junit.Test


class TestOrder: BaseTest() {

    @Test
    fun test_next_and_previous_button() {
        openTrackFromRecyclerview(1)
        checkPlayerIsOpened()
        checkTextInPlayer("Title 1")
        nextButton()
        checkTextInPlayer("Title 2")
        nextButton()
        checkTextInPlayer("Title 3")
        previousButton()
        checkTextInPlayer("Title 2")
        previousButton()
        checkTextInPlayer("Title 1")
        previousButton()
        checkTextInPlayer("Title 1")
        previousButton()
        checkTextInPlayer("Title 1")
        for (i in 2..10) {
            nextButton()
            checkTextInPlayer("Title $i")
        }
        nextButton()
        checkTextInPlayer("Title 10")
        nextButton()
        checkTextInPlayer("Title 10")
    }

    @Test
    fun test_loop_button() {
        openTrackFromRecyclerview(1)
        checkTextInPlayer("Title 1")
        clickOnLoop()
        previousButton()
        checkTextInPlayer("Title 10")
        nextButton()
        checkTextInPlayer("Title 1")
    }
}