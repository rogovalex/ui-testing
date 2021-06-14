package com.example.uitesting.allure

import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.model.Status
import io.qameta.allure.kotlin.model.StepResult
import io.qameta.allure.kotlin.util.ExceptionUtils
import io.qameta.allure.kotlin.util.ResultsUtils
import java.util.*

fun <T> step(name: String, block: Allure.StepContext.() -> T): T {
    val uuid = UUID.randomUUID().toString()
    Allure.lifecycle.startStep(uuid, StepResult().apply {
        this.name = name
    })

    return try {
        block(DefaultStepContext(uuid)).also {
            Allure.lifecycle.updateStep(uuid) { it.status = Status.PASSED }
        }
    } catch (throwable: Throwable) {
        Allure.lifecycle.updateStep { result ->
            with(result) {
                if (StepContext.skipOnFailures) {
                    status = Status.SKIPPED
                } else {
                    status = ResultsUtils.getStatus(throwable) ?: Status.BROKEN
                    statusDetails = ResultsUtils.getStatusDetails(throwable)
                }
            }
        }
        ExceptionUtils.sneakyThrow<RuntimeException>(throwable)
    } finally {
        Allure.lifecycle.stopStep(uuid)
    }
}

/**
 * Basic implementation of step context.
 */
private class DefaultStepContext(private val uuid: String) : Allure.StepContext {

    override fun name(name: String) {
        Allure.lifecycle.updateStep(uuid) { it.name = name }
    }

    override fun <T> parameter(name: String, value: T): T {
        val param = ResultsUtils.createParameter(name, value)
        Allure.lifecycle.updateStep(uuid) { it.parameters.add(param) }
        return value
    }
}
