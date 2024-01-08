package com.maxim.musicplayer

import android.support.test.uiautomator.UiDevice
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.maxim.musicplayer.main.MainActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    private val devise = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    protected fun openTrackFromRecyclerview(index: Int) {
        onView(RecyclerViewMatcher(R.id.audioRecyclerView).itemViewAtIndex(index))
            .perform(click())
    }

    protected fun openTrackFromFavoriteRecyclerview(index: Int) {
        onView(RecyclerViewMatcher(R.id.favoriteRecyclerView).itemViewAtIndex(index))
            .perform(click())
    }

    protected fun checkPlayerIsOpened() {
        onView(withId(R.id.seekBar)).check(matches(isDisplayed()))
    }

    protected fun checkTextInPlayer(expected: String) {
        onView(withId(R.id.playerTitleTextView)).check(matches(withText(expected)))
    }

    protected fun nextButton() {
        onView(withId(R.id.nextButton)).perform(click())
    }

    protected fun previousButton() {
        onView(withId(R.id.previousButton)).perform(click())
    }

    protected fun loopButton() {
        onView(withId(R.id.loopOrderButton)).perform(click())
    }

    protected fun randomButton() {
        onView(withId(R.id.randomOrderButton)).perform(click())
    }

    protected fun back() {
        devise.pressBack()
    }

    protected fun favoriteButton() {
        onView(withId(R.id.favoriteButton)).perform(click())
    }

    protected fun checkIsFavorite(expected: Boolean) {
        val drawable = if (expected) R.drawable.favorite_24 else R.drawable.favorite_border_24
        onView(withId(R.id.favoriteButton)).check(matches(withDrawable(drawable)))
    }

    protected fun swipeToRight() {
        onView(withId(R.id.viewPager)).perform(swipeRight())
    }

    protected fun swipeToLeft() {
        onView(withId(R.id.viewPager)).perform(swipeLeft())
    }

    private fun withDrawable(resourceId: Int): Matcher<View?> {
        return DrawableMatcher(resourceId)
    }
}