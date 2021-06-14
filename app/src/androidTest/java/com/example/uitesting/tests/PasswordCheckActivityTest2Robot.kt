package com.example.uitesting.tests

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.uitesting.robot.passwordCheck
import com.example.uitesting.robot.passwordCheckResult
import com.example.uitesting.rule.OkHttpIdlingResourceRule
import com.example.uitesting.ui.PasswordCheckActivity
import com.example.uitesting.webserver.configureWebServer

import org.junit.Test

import org.junit.Rule
import org.junit.rules.RuleChain

@LargeTest
class PasswordCheckActivityTest2Robot {

    @JvmField
    @Rule
    var ruleChain = RuleChain.outerRule(ActivityTestRule(PasswordCheckActivity::class.java, true))
        .around(OkHttpIdlingResourceRule())

    @Test
    fun checkWeakPassword() {
        val password = "12345"

        configureWebServer {
            registerPasswordCheck(password, "WEAK")
        }

        passwordCheck {
            typePassword(password)
            clickCheckButton()
        }

        passwordCheckResult {
            checkResult("Слабый")
        }
    }
}
