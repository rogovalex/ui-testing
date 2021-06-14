package com.example.uitesting.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import org.hamcrest.Matcher

open class Robot<Lookup>(val lookup: Lookup) {

    fun waitUntil(
        interval: Long = 500L,
        maxAttempts: Int = 20,
        completionBlock: () -> Unit
    ) {
        com.example.uitesting.utils.waitUntil(interval, maxAttempts, completionBlock)
    }

    fun click(viewMatcher: Matcher<View>): ViewInteraction {
        return onView(viewMatcher).perform(click())
    }

    fun typeText(
        viewMatcher: Matcher<View>,
        text: String
    ): ViewInteraction {
        return onView(viewMatcher).perform(typeText(text), closeSoftKeyboard())
    }
}
