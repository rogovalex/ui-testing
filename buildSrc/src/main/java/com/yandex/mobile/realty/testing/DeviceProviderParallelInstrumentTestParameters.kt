@file:Suppress("UnstableApiUsage")

package com.yandex.mobile.realty.testing

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.io.File

interface DeviceProviderParallelInstrumentTestParameters : WorkParameters {
    fun getTestApplicationId(): Property<String>
    fun getInstrumentationRunner(): Property<String>
    fun getSerialNumber(): Property<String>
    fun getNumShards(): Property<Int>
    fun getShardIndex(): Property<Int>
    fun getInstallApks(): ListProperty<File>
    fun getXmlOutputDir(): Property<File>
    fun getAllureOutputDir(): Property<File>
    fun getAdbLocation(): Property<File>
    fun getTestScreenshotsDir(): Property<File>
}
