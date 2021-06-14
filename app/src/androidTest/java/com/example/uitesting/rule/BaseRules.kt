package com.example.uitesting.rule

import com.example.uitesting.allure.StepContext
import io.qameta.allure.android.rules.LogcatRule
import io.qameta.allure.android.rules.ScreenshotRule
import io.qameta.allure.android.rules.WindowHierarchyRule
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

fun baseChainOf(vararg rules: TestRule): RuleChain {
    var chain = RuleChain.outerRule(object : ExternalResource() {
        override fun after() {
            if (StepContext.screenshotsNotFound) {
                StepContext.screenshotsNotFound = false
                error("Expected screenshot(-s) not found. Save actual image(-s) as expected.")
            }
        }
    })
    for (rule in rules) {
        chain = chain.around(rule)
    }

    ScreenshotRule()
    WindowHierarchyRule()
    LogcatRule()

    return chain
        .around(OkHttpIdlingResourceRule())
        .around(ScreenshotRule())
        .around(WindowHierarchyRule())
        .around(LogcatRule())
}
