package com.example.uitesting.named

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher

class NamedViewMatcher(
    private val name: String,
    private val matcher: Matcher<View>
) : Matcher<View> by matcher {

    override fun toString(): String {
        return name
    }

    companion object {

        fun isRoot(): NamedViewMatcher {
            return NamedViewMatcher(
                "является корневым элементом",
                ViewMatchers.isRoot()
            )
        }

        fun withText(text: String): NamedViewMatcher {
            return NamedViewMatcher(
                "имеет текст \"$text\"",
                ViewMatchers.withText(text)
            )
        }

        fun withId(@IdRes idRes: Int): NamedViewMatcher {
            return NamedViewMatcher(
                "имеет id \"${getResourceName(idRes)}\"",
                ViewMatchers.withId(idRes)
            )
        }

        fun isCompletelyDisplayed(): NamedViewMatcher {
            return NamedViewMatcher(
                "полностью отображается",
                ViewMatchers.isCompletelyDisplayed()
            )
        }

        private fun getResourceName(id: Int): String {
            return InstrumentationRegistry.getInstrumentation()
                .targetContext.resources.getResourceEntryName(id)
        }
    }
}