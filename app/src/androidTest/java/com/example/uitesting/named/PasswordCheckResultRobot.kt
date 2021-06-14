package com.example.uitesting.named

import com.example.uitesting.named.NamedViewMatcher.Companion.withText
import com.example.uitesting.named.NamedViewAssertion.Companion.matches
import com.example.uitesting.named.NamedViewInteraction.Companion.onView
import com.example.uitesting.allure.step
import com.example.uitesting.named.NamedViewMatcher.Companion.isCompletelyDisplayed
import com.example.uitesting.named.NamedViewMatcher.Companion.isRoot

fun passwordCheckResult(func: PasswordCheckResultRobot.() -> Unit): PasswordCheckResultRobot {
    return step("На экране Результат проверки пароля") {
        PasswordCheckResultRobot().apply(func)
    }
}

class PasswordCheckResultRobot : Robot<PasswordCheckResultLookup>(PasswordCheckResultLookup) {

    fun isResultVisible() {
        onView(lookup.matchesResultView())
            .check(matches(isCompletelyDisplayed()))
    }

    fun checkResult(result: String) {
        onView(lookup.matchesResultView()).check(matches(withText(result)))
    }

    fun isViewStateMatches(filePath: String) {
        onView(isRoot())
            .takeViewScreenshot()
            .check(filePath)
    }
}
