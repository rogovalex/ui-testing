package com.example.uitesting.tests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.uitesting.R
import com.example.uitesting.rule.OkHttpIdlingResourceRule
import com.example.uitesting.ui.PasswordCheckActivity
import com.example.uitesting.webserver.configureWebServer

import org.junit.Test

import org.junit.Rule
import org.junit.rules.RuleChain

@LargeTest
class PasswordCheckActivityTest1Espresso {

    @get:Rule
    var ruleChain = RuleChain.outerRule(ActivityTestRule(PasswordCheckActivity::class.java, true))
        .around(OkHttpIdlingResourceRule())

    @Test
    fun checkWeakPassword() {
        val password = "12345"

        configureWebServer {
            registerPasswordCheck(password, "WEAK")
        }

        onView(withId(R.id.passwordView))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.checkButton))
            .perform(click())

        onView(withId(R.id.resultView))
            .check(matches(withText("Слабый")))
    }
}
