package com.maxim.musicplayer

import org.junit.Test

class PlayNextTest: BaseTest() {

    @Test
    fun test_play_next_empty_order() {
        openMoreFromRecyclerview(5)
        playNext()
        openTrackFromRecyclerview(3)
        checkTextInPlayer("Title 3")
        nextButton()
        checkTextInPlayer("Title 4")
        nextButton()
        checkTextInPlayer("Title 5")
        nextButton()
        checkTextInPlayer("Title 6")
    }

    @Test
    fun test_play_next() {
        openTrackFromRecyclerview(3)
        checkTextInPlayer("Title 3")
        back()
        openMoreFromRecyclerview(5)
        playNext()
        goToPlayerFromDownBar()
        checkTextInPlayer("Title 3")
        nextButton()
        checkTextInPlayer("Title 5")
        nextButton()
        checkTextInPlayer("Title 4")
    }
}