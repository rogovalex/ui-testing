@file:Suppress("UNCHECKED_CAST")

package com.example.uitesting.screen

import android.view.View
import androidx.test.espresso.UiController
import com.example.uitesting.named.NamedViewAssertion.Companion.matches
import com.example.uitesting.named.NamedViewInteraction
import com.example.uitesting.named.NamedViewInteraction.Companion.onView
import com.example.uitesting.named.NamedViewMatcher
import org.hamcrest.Matcher

open class TBaseView<out T : TBaseView<T>>(val matcher: Matcher<View>) {

    protected val interaction: NamedViewInteraction
        get() = onView(matcher)

    operator fun invoke(function: T.() -> Unit) {
        function(this as T)
    }

    fun click() {
        interaction.click()
    }

    fun isCompletelyDisplayed() {
        interaction.check(matches(NamedViewMatcher.isCompletelyDisplayed()))
    }

    fun isViewStateMatches(
        key: String,
        beforeCapture: (UiController, View) -> Unit = { _, _ -> }
    ) {
        interaction
            .takeViewScreenshot(beforeCapture)
            .check(key)
    }
}
