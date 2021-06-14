package com.example.uitesting.tests

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.uitesting.rule.baseChainOf
import com.example.uitesting.screen.PasswordCheckResultScreen
import com.example.uitesting.screen.PasswordCheckScreen
import com.example.uitesting.screen.Screen.Companion.onScreen
import com.example.uitesting.ui.PasswordCheckActivity
import com.example.uitesting.webserver.configureWebServer
import io.qameta.allure.kotlin.Description

import org.junit.Test

import org.junit.Rule

@LargeTest
class PasswordCheckActivityTest6Screen {

    @JvmField
    @Rule
    var ruleChain = baseChainOf(ActivityTestRule(PasswordCheckActivity::class.java, true))

    @Test
    @Description("Проверяем слабый пароль")
    fun checkWeakPassword() {
        val password = "12345"

        configureWebServer {
            registerPasswordCheck(password, "WEAK")
        }

        onScreen<PasswordCheckScreen> {
            passwordView.typeText(password)
            checkButton.click()
        }

        onScreen<PasswordCheckResultScreen> {
            resultView.waitUntil { isCompletelyDisplayed() }
            root.isViewStateMatches("checkWeakPassword")
        }
    }
}
