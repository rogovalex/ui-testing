package com.example.uitesting.screenshot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.uitesting.allure.StepContext
import com.example.uitesting.allure.step
import com.example.uitesting.named.diff
import io.qameta.allure.android.AllureAndroidLifecycle
import java.io.*
import kotlin.math.max

class ScreenshotInteraction(
    private val name: String,
    private val screenshotResult: ScreenshotResult
) {

    fun check(key: String) {
        when (screenshotResult) {
            is ScreenshotResult.SingleScreenshot -> {
                step("Сравниваем скриншот '$name' с эталоном") {
                    compareWithExpected(screenshotResult.bitmap, key)
                }
            }
            is ScreenshotResult.Screenshots -> {
                step("Сравниваем скриншоты '$name' с эталонами") {
                    compareWithExpected(screenshotResult.bitmaps, key)
                }
            }
        }
    }

    private fun compareWithExpected(actualBitmap: Bitmap, key: String) {
        val expectedScreenshotFile = File(SCREENSHOTS_DIR, "$key.png")
        val expectedBitmap = loadImageFromDisk(expectedScreenshotFile)
        if (expectedBitmap != null) {
            assertBitmap(key, expectedBitmap, actualBitmap)
        } else {
            saveImageOnDisk(expectedScreenshotFile, actualBitmap, Bitmap.CompressFormat.PNG)
            StepContext.screenshotsNotFound = true
        }
    }

    private fun getFilenameForIndex(filenamePrefix: String, index: Int): String {
        return "$filenamePrefix$index.png"
    }

    private fun compareWithExpected(actualBitmaps: List<Bitmap>, key: String) {
        val (screenshotsDir, rawFilenamePrefix) =
            File(SCREENSHOTS_DIR, key).run { parentFile to name }
        val filenamePrefix = getFilenamePrefix(rawFilenamePrefix)

        val expectedFiles = screenshotsDir
            ?.listFiles { _, name -> name.contains(filenamePrefix) }
            ?.associateByTo(mutableMapOf()) { it.name }

        if (!expectedFiles.isNullOrEmpty()) {
            val screenshotsCount = max(actualBitmaps.size, expectedFiles.size)
            for (i in 0 until screenshotsCount) {
                val filename = getFilenameForIndex(filenamePrefix, i)
                val expectedScreenshot = expectedFiles.remove(filename)
                val expectedBitmap = expectedScreenshot?.let(Companion::loadImageFromDisk)
                val actualBitmap = actualBitmaps.getOrNull(i)
                assertBitmap(key, expectedBitmap, actualBitmap)
            }
        } else {
            actualBitmaps.forEachIndexed { i, bitmap ->
                val filename = getFilenameForIndex(filenamePrefix, i)
                val screenshotFile = File(screenshotsDir, filename)
                saveImageOnDisk(screenshotFile, bitmap, Bitmap.CompressFormat.PNG)
            }
            StepContext.screenshotsNotFound = true
        }
    }

    private fun getFilenamePrefix(filename: String): String {
        return "${filename}_"
    }

    private fun assertBitmap(key: String, expected: Bitmap?, actual: Bitmap?) {
        when {
            expected == null && actual != null -> {
                attachImageToReport("actual", actual)
                throw AssertionError("There is excess screenshot of view for $key")
            }
            expected != null && actual == null -> {
                attachImageToReport("expected", expected)
                throw AssertionError("Expecting screenshot but nothing found for $key")
            }
            expected != null && actual != null -> {
                val diff = diff(expected, actual)
                if (diff != null) {
                    attachImageToReport("expected", expected)
                    attachImageToReport("actual", actual)
                    attachImageToReport("diff", diff)
                    throw AssertionError("The view doesn't match the screenshot for $key")
                } else {
                    attachImageToReport("expected", expected)
                }
            }
            else -> error("Both expected and actual screenshots are null for $key")
        }
    }

    private fun attachImageToReport(filename: String, bmp: Bitmap) {
        val bytes = ByteArrayOutputStream().use { output ->
            bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.toByteArray()
        }
        AllureAndroidLifecycle.addAttachment(
            name = filename,
            stream = ByteArrayInputStream(bytes),
            type = IMAGE_PNG,
            fileExtension = PNG_EXTENSION,
        )
    }

    private companion object {
        const val EXTERNAL_DIR = "/sdcard"
        const val SCREENSHOTS_DIR = "$EXTERNAL_DIR/realty_screenshots"
        private const val IMAGE_PNG = "image/png"
        private const val PNG_EXTENSION = ".png"

        fun loadImageFromDisk(file: File): Bitmap? {
            return if (file.exists()) {
                file.inputStream().use { stream ->
                    BitmapFactory.decodeStream(stream, null, BitmapFactory.Options())
                }
            } else {
                null
            }
        }

        fun saveImageOnDisk(file: File, bitmap: Bitmap, format: Bitmap.CompressFormat) {
            if (file.exists()) {
                file.delete()
            }
            file.parentFile?.mkdirs()
            BufferedOutputStream(FileOutputStream(file)).use { bos ->
                bitmap.compress(format, 100, bos)
            }
        }
    }
}
