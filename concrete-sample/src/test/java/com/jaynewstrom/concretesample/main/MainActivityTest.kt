package com.jaynewstrom.concretesample.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
