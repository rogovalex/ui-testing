package com.example.uitesting.screen

import android.view.View
import org.hamcrest.Matcher

class TEditText(matcher: Matcher<View>) : TBaseView<TEditText>(matcher) {

    fun typeText(text: String) {
        interaction.typeText(text)
    }
}
