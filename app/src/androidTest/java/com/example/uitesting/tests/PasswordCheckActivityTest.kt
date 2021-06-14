package com.example.uitesting.tests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.uitesting.R
import com.example.uitesting.ui.PasswordCheckActivity

import org.junit.Test

import org.junit.Rule

@LargeTest
class PasswordCheckActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(PasswordCheckActivity::class.java)

    @Test
    fun checkWeakPassword() {
        onView(withId(R.id.passwordView))
            .perform(typeText("12345"), closeSoftKeyboard())

        onView(withId(R.id.checkButton))
            .perform(click())

        onView(withId(R.id.resultView))
            .check(matches(withText("Слабый")))
    }
}
