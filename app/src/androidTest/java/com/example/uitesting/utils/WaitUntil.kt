package com.example.uitesting.utils

import com.example.uitesting.allure.StepContext

fun waitUntil(
    interval: Long = 500L,
    maxAttempts: Int = 20,
    completionBlock: () -> Unit
) {
    require(maxAttempts > 1) { "Max attempts count should be greater than 1" }

    StepContext.skipOnFailures = true
    var attempt = 1

    while (true) {
        val result = runCatching(completionBlock)
        if (result.isSuccess) {
            StepContext.skipOnFailures = false
            break
        }
        attempt++
        if (attempt == maxAttempts) {
            StepContext.skipOnFailures = false
        } else if (attempt > maxAttempts) {
            throw AssertionError(
                "Completion failed after $maxAttempts attempts",
                result.exceptionOrNull()
            )
        }
        Thread.sleep(interval)
    }
}
