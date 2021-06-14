package com.example.uitesting.named

import android.view.View
import com.example.uitesting.named.NamedViewInteraction.Companion.onView
import org.hamcrest.Matcher

open class Robot<Lookup>(val lookup: Lookup) {

    fun waitUntil(
        interval: Long = 500L,
        maxAttempts: Int = 20,
        completionBlock: () -> Unit
    ) {
        com.example.uitesting.utils.waitUntil(interval, maxAttempts, completionBlock)
    }

    fun click(viewMatcher: Matcher<View>): NamedViewInteraction {
        return onView(viewMatcher).click()
    }

    fun typeText(
        viewMatcher: Matcher<View>,
        text: String
    ): NamedViewInteraction {
        return onView(viewMatcher).typeText(text)
    }
}
