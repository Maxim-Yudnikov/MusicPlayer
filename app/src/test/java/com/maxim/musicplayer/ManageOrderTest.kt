package com.maxim.musicplayer

import com.maxim.musicplayer.audioList.presentation.AudioUi
import com.maxim.musicplayer.cope.SimpleStorage
import com.maxim.musicplayer.player.media.ManageOrder
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ManageOrderTest {
    private lateinit var manageOrder: ManageOrder
    private lateinit var storage: FakeSimpleStorage
    private lateinit var trackOne: AudioUi
    private lateinit var trackTwo: AudioUi
    private lateinit var trackThree: AudioUi

    @Before
    fun before() {
        storage = FakeSimpleStorage()
        manageOrder = ManageOrder.Base(storage)
        trackOne = AudioUi.Base(
            23, "Title1", "Artist", 55, "album", null, null
        )
        trackTwo = AudioUi.Base(
            24, "Title2", "Artist", 55, "album", null, null
        )
        trackThree = AudioUi.Base(
            25, "Title3", "Artist", 55, "album", null, null
        )
    }

    @Test
    fun test_from_0_to_last() {
        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 0)

        var actual = manageOrder.next()
        assertEquals(trackTwo, actual)

        actual = manageOrder.next()
        assertEquals(trackThree, actual)

        actual = manageOrder.next()
        assertEquals(trackThree, actual)
    }

    @Test
    fun test_from_0_to_last_loop() {
        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 0)
        manageOrder.isLoop = true

        var actual = manageOrder.next()
        assertEquals(trackTwo, actual)

        actual = manageOrder.next()
        assertEquals(trackThree, actual)

        actual = manageOrder.next()
        assertEquals(trackOne, actual)

        actual = manageOrder.next()
        assertEquals(trackTwo, actual)

        actual = manageOrder.next()
        assertEquals(trackThree, actual)

        actual = manageOrder.next()
        assertEquals(trackOne, actual)
    }

    @Test
    fun test_from_last_to_0() {
        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 2)

        var actual = manageOrder.previous()
        assertEquals(trackTwo, actual)

        actual = manageOrder.previous()
        assertEquals(trackOne, actual)

        actual = manageOrder.previous()
        assertEquals(trackOne, actual)
    }

    @Test
    fun test_from_last_to_0_loop() {
        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 2)
        manageOrder.isLoop = true

        var actual = manageOrder.previous()
        assertEquals(trackTwo, actual)

        actual = manageOrder.previous()
        assertEquals(trackOne, actual)

        actual = manageOrder.previous()
        assertEquals(trackThree, actual)
    }
}

class FakeSimpleStorage : SimpleStorage {
    //private val map = mutableMapOf<String, Boolean>()
    override fun save(key: String, value: Boolean) {
        //map[key] = value
    }

    override fun read(key: String, default: Boolean) =
        false //if (map[key] == null) default else map[key]!!
}