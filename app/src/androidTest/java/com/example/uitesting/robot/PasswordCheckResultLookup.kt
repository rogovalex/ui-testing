package com.example.uitesting.robot

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.example.uitesting.R
import org.hamcrest.Matcher

object PasswordCheckResultLookup {

    fun matchesResultView(): Matcher<View> {
        return ViewMatchers.withId(R.id.resultView)
    }
}
