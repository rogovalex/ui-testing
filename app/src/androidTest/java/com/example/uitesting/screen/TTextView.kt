package com.example.uitesting.screen

import android.view.View
import com.example.uitesting.named.NamedViewMatcher.Companion.withText
import com.example.uitesting.named.NamedViewAssertion.Companion.matches
import org.hamcrest.Matcher

class TTextView(matcher: Matcher<View>) : TBaseView<TTextView>(matcher) {

    fun isTextEquals(text: String) {
        interaction.check(matches(withText(text)))
    }
}
