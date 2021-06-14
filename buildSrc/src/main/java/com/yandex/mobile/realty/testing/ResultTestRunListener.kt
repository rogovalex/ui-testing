package com.yandex.mobile.realty.testing

import com.android.ddmlib.testrunner.ITestRunListener
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner
import com.android.ddmlib.testrunner.TestIdentifier
import com.android.ddmlib.testrunner.XmlTestRunListener
import java.io.File

class ResultTestRunListener(
        private val runName: String,
        private val runnerProvider: () -> RemoteAndroidTestRunner
) : XmlTestRunListener() {

    private val retryFilters = listOf(
            { trace: String -> trace.startsWith(PROCESS_CRASHED) },
            { trace: String -> trace.startsWith(CONDITION_TIMED_OUT) },
            { trace: String -> trace.startsWith(PERFORM_EXCEPTION) }
    )
    private val testsToRetry = HashSet<TestIdentifier>()

    override fun getResultFile(reportDir: File): File {
       return File(reportDir, "TEST-$runName.xml")
    }

    override fun testFailed(test: TestIdentifier, trace: String) {
        if (retryFilters.any { filter -> filter.invoke(trace) }) {
            println("Test $test failed with trace= $trace")
            testsToRetry.add(test)
        }
        superTestFailed(test, trace)
    }

    override fun testRunEnded(elapsedTime: Long, runMetrics: MutableMap<String, String>) {
        if (testsToRetry.isNotEmpty()) {
            runResult.testRunEnded(elapsedTime, runMetrics)

            val retryRunner = runnerProvider.invoke()
            retryRunner.setClassNames(testsToRetry.map(TestIdentifier::toString).toTypedArray())

            val delegate = this

            retryRunner.run(object : ITestRunListener by delegate {

                override fun testRunStarted(runName: String, testCount: Int) {
                    runResult.isRunComplete = false
                    runResult.setAggregateMetrics(true)
                }

                override fun testFailed(test: TestIdentifier, trace: String) {
                    delegate.superTestFailed(test, trace)
                }

                override fun testEnded(test: TestIdentifier, testMetrics: MutableMap<String, String>) {
                    testsToRetry.remove(test)
                    delegate.testEnded(test, testMetrics)
                }

                override fun testRunEnded(elapsedTime: Long, runMetrics: MutableMap<String, String>) {
                    delegate.superTestRunEnded(elapsedTime, runMetrics)
                }
            })
        } else {
            superTestRunEnded(elapsedTime, runMetrics)
        }
    }

    private fun superTestFailed(test: TestIdentifier, trace: String) {
        super.testFailed(test, trace)
    }

    private fun superTestRunEnded(elapsedTime: Long, runMetrics: MutableMap<String, String>) {
        super.testRunEnded(elapsedTime, runMetrics)
    }

    companion object {

        private const val PROCESS_CRASHED = "Test instrumentation process crashed."
        private const val CONDITION_TIMED_OUT = "java.lang.AssertionError: Completion failed after"
        private const val PERFORM_EXCEPTION =
                "androidx.test.espresso.PerformException: Error performing"
    }
}
