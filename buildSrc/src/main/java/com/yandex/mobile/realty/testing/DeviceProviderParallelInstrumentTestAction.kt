@file:Suppress("UnstableApiUsage", "SdCardPath")
package com.yandex.mobile.realty.testing

import com.android.builder.testing.api.DeviceConnector
import com.android.ddmlib.testrunner.AndroidTestOrchestratorRemoteAndroidTestRunner
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner
import com.android.utils.NullLogger
import org.gradle.workers.WorkAction
import java.io.File

val TIMEOUT = 60000

abstract class DeviceProviderParallelInstrumentTestAction :
    WorkAction<DeviceProviderParallelInstrumentTestParameters> {

    override fun execute() {
        val adbLocation = parameters.getAdbLocation().get()
        val devices = with(ConnectedDeviceProvider(adbLocation, 60000, NullLogger())) {
            init()
            devices.also { terminate() }
        }

        val serialNumber = parameters.getSerialNumber().get()
        val device = devices.first { it.serialNumber == serialNumber }

        parameters.getInstallApks().get().forEach { file -> installPackage(serialNumber, file) }

        updateSetting(serialNumber, "window_animation_scale", "0")
        updateSetting(serialNumber, "transition_animation_scale", "0")
        updateSetting(serialNumber, "animator_duration_scale", "0")

        val screenshotsPath = "/sdcard/realty_screenshots"
        createDirectory(serialNumber, screenshotsPath)
        clearDirectory(serialNumber, screenshotsPath)
        pushDirectory(
            serialNumber,
            screenshotsPath,
            "${parameters.getTestScreenshotsDir().get().absolutePath}/."
        )

        val allureResultsPath = "/sdcard/allure-results"
        createDirectory(serialNumber, allureResultsPath)
        clearDirectory(serialNumber, allureResultsPath)

        val runName = device.name
        val runner = createTestRunner(device, runName)
        runner.addInstrumentationArg("numShards", "${parameters.getNumShards().get()}")
        runner.addInstrumentationArg("shardIndex", "${parameters.getShardIndex().get()}")

        val runListener = ResultTestRunListener(runName) { createTestRunner(device, runName) }
        runListener.setReportDir(parameters.getXmlOutputDir().get())

        runner.run(runListener)

        pullDirectory(
            serialNumber,
            allureResultsPath,
            parameters.getAllureOutputDir().get().absolutePath
        )
    }

    private fun createTestRunner(
        device: DeviceConnector,
        runName: String
    ): RemoteAndroidTestRunner {
        val runner = AndroidTestOrchestratorRemoteAndroidTestRunner(
            parameters.getTestApplicationId().get(),
            parameters.getInstrumentationRunner().orNull,
            device,
            true
        )
        runner.setDebug(false)
        runner.addBooleanArg("clearPackageData", true)
        runner.setRunName(runName)
        return runner
    }

    private fun installPackage(serialNumber: String, file: File) {
        println("$serialNumber installing '$file'")
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "install", "-r", "-t", file.absolutePath
            )
            .start()
            .waitFor()
    }

    private fun pushDirectory(serialNumber: String, remotePath: String, localPath: String) {
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "push", localPath, remotePath
            )
            .start()
            .waitFor()
    }

    private fun pullDirectory(serialNumber: String, remotePath: String, localPath: String) {
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "pull", remotePath, localPath
            )
            .start()
            .waitFor()
    }

    private fun updateSetting(serialNumber: String, key: String, value: String) {
        println("$serialNumber setting '$key' to '$value'")
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "shell", "-n", "settings", "put", "global", key, value
            )
            .start()
            .waitFor()
    }

    private fun createDirectory(serialNumber: String, remotePath: String) {
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "shell", "mkdir", remotePath
            )
            .start()
            .waitFor()
    }

    private fun clearDirectory(serialNumber: String, remotePath: String) {
        ProcessBuilder()
            .command(
                parameters.getAdbLocation().get().path,
                "-s", serialNumber,
                "shell", "rm", "-rf", remotePath
            )
            .start()
            .waitFor()
    }
}
