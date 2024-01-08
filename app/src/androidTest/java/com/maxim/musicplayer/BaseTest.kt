package com.maxim.musicplayer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxim.musicplayer.main.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    protected fun openTrackFromRecyclerview(index: Int) {
        onView(RecyclerViewMatcher(R.id.audioRecyclerView).itemViewAtIndex(index))
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

    protected fun clickOnLoop() {
        onView(withId(R.id.loopOrderButton)).perform(click())
    }
}