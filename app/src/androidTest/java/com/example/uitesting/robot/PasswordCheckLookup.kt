package com.example.uitesting.robot

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.example.uitesting.R
import org.hamcrest.Matcher

object PasswordCheckLookup {

    fun matchesPasswordView(): Matcher<View> {
        return ViewMatchers.withId(R.id.passwordView)
    }

    fun matchesCheckButton(): Matcher<View> {
        return ViewMatchers.withId(R.id.checkButton)
    }
}
