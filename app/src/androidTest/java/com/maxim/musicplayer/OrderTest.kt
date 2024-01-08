package com.maxim.musicplayer

import org.junit.Test


class OrderTest: BaseTest() {

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
        loopButton()
        previousButton()
        checkTextInPlayer("Title 10")
        nextButton()
        checkTextInPlayer("Title 1")
    }

    @Test
    fun test_random_button() {
        openTrackFromRecyclerview(1)
        checkTextInPlayer("Title 1")
        randomButton()
        nextButton()
        checkTextInPlayer("Title 3")
        nextButton()
        checkTextInPlayer("Title 5")
        nextButton()
        checkTextInPlayer("Title 7")
        randomButton()
        nextButton()
        checkTextInPlayer("Title 8")
        previousButton()
        checkTextInPlayer("Title 7")
        previousButton()
        checkTextInPlayer("Title 6")
        randomButton()
        nextButton()
        checkTextInPlayer("Title 1")
        previousButton()
        checkTextInPlayer("Title 6")
        //check not looped
        repeat(2) {
            previousButton()
            checkTextInPlayer("Title 6")
        }
    }

    @Test
    fun test_loop_and_random_buttons() {
        openTrackFromRecyclerview(1)
        checkTextInPlayer("Title 1")
        loopButton()
        randomButton()
        previousButton()
        checkTextInPlayer("Title 10")
        previousButton()
        checkTextInPlayer("Title 8")
        repeat(2) {
            loopButton()
        }
        nextButton()
        checkTextInPlayer("Title 10")
        checkTextInPlayer("Title 10")
        loopButton()
        nextButton()
        checkTextInPlayer("Title 1")
        previousButton()
        checkTextInPlayer("Title 10")
        repeat(2) {
            loopButton()
            randomButton()
        }
        previousButton()
        checkTextInPlayer("Title 10")
        previousButton()
        checkTextInPlayer("Title 10")
        loopButton()
        previousButton()
        checkTextInPlayer("Title 8")
        randomButton()
        previousButton()
        checkTextInPlayer("Title 7")
    }

    @Test
    fun test_open_different_tracks() {
        openTrackFromRecyclerview(5)
        checkTextInPlayer("Title 5")
        back()
        openTrackFromRecyclerview(6)
        checkTextInPlayer("Title 6")
        previousButton()
        checkTextInPlayer("Title 5")
    }

    @Test
    fun test_open_different_tracks_with_random() {
        openTrackFromRecyclerview(2)
        checkTextInPlayer("Title 2")
        randomButton()
        previousButton()
        checkTextInPlayer("Title 2")
        back()
        openTrackFromRecyclerview(5)
        checkTextInPlayer("Title 5")
        previousButton()
        checkTextInPlayer("Title 5")
        nextButton()
        checkTextInPlayer("Title 1")
        previousButton()
        randomButton()
        nextButton()
        checkTextInPlayer("Title 6")
    }

    @Test
    fun test_add_to_favorite() {
        openTrackFromRecyclerview(1)
        checkIsFavorite(false)
        favoriteButton()
        checkIsFavorite(true)
        back()
        openTrackFromRecyclerview(2)
        checkIsFavorite(false)
        previousButton()
        checkIsFavorite(true)
        back()
        openTrackFromRecyclerview(6)
        checkIsFavorite(false)
        randomButton()
        nextButton()
        checkIsFavorite(true)
    }

    @Test
    fun test_add_to_favorite_list() {
        openTrackFromRecyclerview(5)
        checkTextInPlayer("Title 5")
        favoriteButton()
        checkIsFavorite(true)
        back()

        swipeToLeft()
        Thread.sleep(500)

        openTrackFromFavoriteRecyclerview(1)
        checkTextInPlayer("Title 5")
        checkIsFavorite(true)
    }

    @Test
    fun test_add_to_favorite_list_several_and_remove_one() {
        openTrackFromRecyclerview(5)
        favoriteButton()
        back()
        openTrackFromRecyclerview(7)
        favoriteButton()
        back()
        openTrackFromRecyclerview(9)
        favoriteButton()
        back()

        swipeToLeft()
        Thread.sleep(500)

        openTrackFromFavoriteRecyclerview(1)
        checkTextInPlayer("Title 5")
        checkIsFavorite(true)
        nextButton()
        checkTextInPlayer("Title 7")
        checkIsFavorite(true)
        nextButton()
        checkTextInPlayer("Title 9")
        checkIsFavorite(true)
        nextButton()
        checkTextInPlayer("Title 9")
        loopButton()
        nextButton()
        checkTextInPlayer("Title 5")

        favoriteButton()
        checkTextInPlayer("Title 7")
        nextButton()
        checkTextInPlayer("Title 9")
        nextButton()
        checkTextInPlayer("Title 7")
    }
}