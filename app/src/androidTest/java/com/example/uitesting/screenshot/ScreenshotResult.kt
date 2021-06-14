package com.example.uitesting.screenshot

import android.graphics.Bitmap

sealed class ScreenshotResult {

    class SingleScreenshot(val bitmap: Bitmap) : ScreenshotResult()

    class Screenshots(val bitmaps: List<Bitmap>) : ScreenshotResult()
}
