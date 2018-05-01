package com.jaynewstrom.concretesample.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jaynewstrom.concretesample.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test fun ensureActivityIsStarted() {
        onView(withId(R.id.first_details_button)).check(matches(isDisplayed()))
    }
}
