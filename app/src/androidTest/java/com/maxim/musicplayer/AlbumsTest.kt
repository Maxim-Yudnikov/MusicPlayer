package com.maxim.musicplayer

import org.junit.Test

class AlbumsTest: BaseTest() {

    @Test
    fun test_open_albums() {
        albumsScreen()
        openAlbumAtPosition(0)
        checkAlbumTitle("Album 0")
        back()
        openAlbumAtPosition(1)
        checkAlbumTitle("Album 1")
        back()
        openAlbumAtPosition(1)
        checkAlbumTitle("Album 2")
    }

    @Test
    fun test_album_order() {
        albumsScreen()
        openAlbumAtPosition(0)
        openTrackFromFavoriteRecyclerview(1)
        checkTextInPlayer("Title 1")
        nextButton()
        checkTextInPlayer("Title 2")
        nextButton()
        checkTextInPlayer("Title 3")
        nextButton()
        checkTextInPlayer("Title 4")
        nextButton()
        checkTextInPlayer("Title 4")
        loopButton()
        nextButton()
        checkTextInPlayer("Title 1")
    }

    @Test
    fun test_add_to_favorite_from_album() {
        albumsScreen()
        openAlbumAtPosition(0)
        openTrackFromFavoriteRecyclerview(1)
        favoriteButton()
        back()
        openTrackFromFavoriteRecyclerview(2)
        checkIsFavorite(false)
        previousButton()
        checkIsFavorite(true)
    }
}