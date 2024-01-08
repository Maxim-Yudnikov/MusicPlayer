package com.maxim.musicplayer
//
//import android.media.MediaPlayer
//import android.net.Uri
//import android.widget.TextView
//import com.maxim.musicplayer.audioList.presentation.AudioUi
//import com.maxim.musicplayer.cope.data.SimpleStorage
//import com.maxim.musicplayer.player.media.LoopState
//import com.maxim.musicplayer.player.media.ManageOrder
//import junit.framework.TestCase.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class ManageOrderTest {
//    private lateinit var manageOrder: ManageOrder
//    private lateinit var storage: FakeSimpleStorage
//    private lateinit var trackOne: AudioUi.Abstract
//    private lateinit var trackTwo: AudioUi.Abstract
//    private lateinit var trackThree: AudioUi.Abstract
//
//    @Before
//    fun before() {
//        storage = FakeSimpleStorage()
//        manageOrder = ManageOrder.Base(storage)
//        trackOne = FakeAudio(23)
//        trackTwo = FakeAudio(24)
//        trackThree = FakeAudio(25)
//    }
//
//    @Test
//    fun test_from_0_to_last() {
//        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 0)
//
//        var actual = manageOrder.next()
//        assertEquals(trackTwo, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackThree, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackThree, actual)
//    }
//
//    @Test
//    fun test_from_0_to_last_loop() {
//        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 0)
//        manageOrder.changeLoop(MediaPlayer())
//
//        var actual = manageOrder.next()
//        assertEquals(trackTwo, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackThree, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackOne, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackTwo, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackThree, actual)
//
//        actual = manageOrder.next()
//        assertEquals(trackOne, actual)
//    }
//
//    @Test
//    fun test_from_last_to_0() {
//        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 2)
//
//        var actual = manageOrder.previous()
//        assertEquals(trackTwo, actual)
//
//        actual = manageOrder.previous()
//        assertEquals(trackOne, actual)
//
//        actual = manageOrder.previous()
//        assertEquals(trackOne, actual)
//    }
//
//    @Test
//    fun test_from_last_to_0_loop() {
//        manageOrder.generate(listOf(trackOne, trackTwo, trackThree), 2)
//        manageOrder.changeLoop(MediaPlayer())
//
//        var actual = manageOrder.previous()
//        assertEquals(trackTwo, actual)
//
//        actual = manageOrder.previous()
//        assertEquals(trackOne, actual)
//
//        actual = manageOrder.previous()
//        assertEquals(trackThree, actual)
//    }
//}
//
//class FakeSimpleStorage : SimpleStorage {
//
//    override fun save(key: String, value: Boolean) = Unit
//
//    override fun save(key: String, value: LoopState) = Unit
//
//    override fun read(key: String, default: Boolean) = false
//
//    override fun read(key: String, default: LoopState) = LoopState.Base
//}
//
//private data class FakeAudio(private val ownId: Long) :
//    AudioUi.Abstract(ownId, "", "", 555, "", Uri.EMPTY, Uri.EMPTY) {
//    override fun same(item: AudioUi) = false
//    override fun showTitle(textView: TextView) = Unit
//}