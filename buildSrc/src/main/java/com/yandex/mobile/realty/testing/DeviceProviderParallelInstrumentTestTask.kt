@file:Suppress("UnstableApiUsage")

package com.yandex.mobile.realty.testing

import com.android.utils.NullLogger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

open class DeviceProviderParallelInstrumentTestTask @Inject constructor(
    private val workerExecutor: WorkerExecutor,
    private val testApplicationId: String,
    private val instrumentationRunner: String?,
    private val applicationApk: File,
    private val testsApk: File,
    private val testUtilsApk: File,
    private val orchestratorApk: File,
    private val adbLocation: File,
    private val outputDir: File,
    private val testScreenshotsDir: File
) : DefaultTask() {

    @TaskAction
    fun run() {
        val installApks = listOf(applicationApk, testsApk, testUtilsApk, orchestratorApk)

        outputDir.deleteRecursively()
        outputDir.mkdirs()
        val xmlResultsDir = File(outputDir, "xml")
        xmlResultsDir.mkdirs()
        val allureResultsDir = File(outputDir, "allure")
        allureResultsDir.mkdir()

        val devices = with(ConnectedDeviceProvider(adbLocation, 60000, NullLogger())) {
            init()
            devices.also { terminate() }
        }

        val numShards = devices.size

        val workQueue = workerExecutor.noIsolation()
        devices.forEachIndexed { shardIndex, device ->
            workQueue.submit(DeviceProviderParallelInstrumentTestAction::class.java) {
                getTestApplicationId().set(testApplicationId)
                getInstrumentationRunner().set(instrumentationRunner)
                getSerialNumber().set(device.serialNumber)
                getNumShards().set(numShards)
                getShardIndex().set(shardIndex)
                getInstallApks().set(installApks)
                getXmlOutputDir().set(xmlResultsDir)
                getAllureOutputDir().set(allureResultsDir)
                getAdbLocation().set(adbLocation)
                getTestScreenshotsDir().set(testScreenshotsDir)
            }
        }
        workQueue.await()
    }
}
