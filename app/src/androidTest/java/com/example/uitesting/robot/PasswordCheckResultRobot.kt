package com.example.uitesting.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

fun passwordCheckResult(func: PasswordCheckResultRobot.() -> Unit): PasswordCheckResultRobot {
    return PasswordCheckResultRobot().apply(func)
}

class PasswordCheckResultRobot : Robot<PasswordCheckResultLookup>(PasswordCheckResultLookup) {

    fun isResultVisible() {
        onView(lookup.matchesResultView()).check(matches(isCompletelyDisplayed()))
    }

    fun checkResult(result: String) {
        onView(lookup.matchesResultView()).check(matches(withText(result)))
    }
}
