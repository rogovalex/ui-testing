package com.yandex.mobile.realty.testing

import com.yandex.mobile.realty.testing.reporter.ReportType
import com.yandex.mobile.realty.testing.reporter.TestReport
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import java.io.File
import javax.inject.Inject

open class GradleReportTask @Inject constructor(
        private val outputDir: File
) : DefaultTask() {

    @TaskAction
    fun run() {
        val xmlResultsDir = File(outputDir, "xml")
        val reportDir = File(outputDir, "html")
        reportDir.mkdirs()
        val report = TestReport(ReportType.SINGLE_FLAVOR, xmlResultsDir, reportDir)
        report.generateReport()

        val regex = "failures=\"(\\d+)\"".toRegex()
        var failures = 0
        xmlResultsDir.listFiles { file ->
            file.name.startsWith("TEST-") && file.name.endsWith(".xml")
        }?.forEach { file ->
            val line = file.useLines { sequence ->
                sequence.first { it.startsWith("<testsuite") }
            }
            regex.find(line)?.let { matcher ->
                failures += matcher.groupValues[1].toInt()
            }
        }

        if (failures > 0) {
            state.addFailure(
                    TaskExecutionException(
                            this,
                            RuntimeException("Tests execution failed: $failures failure(s)")
                    )
            )
        }
    }
}
