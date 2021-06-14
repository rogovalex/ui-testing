package com.example.uitesting.named

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun diff(expected: Bitmap, actual: Bitmap): Bitmap? {
    val expectedHeight = expected.height
    val expectedWidth = expected.width
    val actualHeight = actual.height
    val actualWidth = actual.width

    val resultWidth = max(expectedWidth, actualWidth)
    val resultHeight = max(expectedHeight, actualHeight)
    val result = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint().apply {
        style = Paint.Style.FILL
    }

    val width = min(expectedWidth, actualWidth)
    val height = min(expectedHeight, actualHeight)
    val expectedPixels = IntArray(width)
    val actualPixels = IntArray(width)
    var diffCount = 0
    for (y in 0 until height) {
        expected.getPixels(expectedPixels, 0, width, 0, y, width, 1)
        actual.getPixels(actualPixels, 0, width, 0, y, width, 1)
        for (x in 0 until width) {
            val pixelDiff = pixelDiff(expectedPixels[x], actualPixels[x])
            when {
                pixelDiff == 0f -> {
                    paint.color = toGrayscale(expectedPixels[x])
                }
                pixelDiff > 0 -> {
                    diffCount++
                    paint.color = Color.RED
                }
                else -> {
                    diffCount++
                    paint.color = Color.YELLOW
                }
            }
            canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
        }
    }

    if (resultWidth > width) {
        diffCount++
        paint.color = Color.RED
        canvas.drawRect(
                width.toFloat(),
                0f,
                resultWidth.toFloat(),
                resultWidth.toFloat(),
                paint
        )
    }

    if (resultHeight > height) {
        diffCount++
        paint.color = Color.RED
        canvas.drawRect(
                0f,
                height.toFloat(),
                resultHeight.toFloat(),
                resultHeight.toFloat(),
                paint
        )
    }

    return if (diffCount > 0) result else null
}

private fun pixelDiff(expected: Int, actual: Int): Float {
    val eRed = Color.red(expected)
    val eGreen = Color.green(expected)
    val eBlue = Color.blue(expected)
    val eAlpha = Color.alpha(expected)

    val aRed = Color.red(actual)
    val aGreen = Color.green(actual)
    val aBlue = Color.blue(actual)
    val aAlpha = Color.alpha(actual)

    return if (eRed == aRed && eGreen == aGreen && eBlue == aBlue && eAlpha == aAlpha) {
        0f
    } else {
        val eBrightness = if (eAlpha < 255) {
            val alpha = eAlpha / 255f
            rgbToY(
                    blend(eRed, alpha),
                    blend(eGreen, alpha),
                    blend(eBlue, alpha)
            )
        } else {
            rgbToY(eRed, eGreen, eBlue)
        }
        val aBrightness = if (aAlpha < 255) {
            val alpha = aAlpha / 255f
            rgbToY(
                    blend(aRed, alpha),
                    blend(aGreen, alpha),
                    blend(aBlue, alpha)
            )
        } else {
            rgbToY(aRed, aGreen, aBlue)
        }
        eBrightness - aBrightness
    }
}

private fun toGrayscale(color: Int, resultAlpha: Float = 0.3f): Int {
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    val alpha = Color.alpha(color)
    val brightness = rgbToY(red, green, blue).roundToInt()
    val result = blend(brightness, alpha / 255f * resultAlpha)
    return (255).shl(24) + result.shl(16) + result.shl(8) + result
}

private fun rgbToY(red: Int, green: Int, blue: Int): Float {
    return red * 0.29889531f + green * 0.58662247f + blue * 0.11448223f
}

private fun blend(color: Int, alpha: Float): Int {
    return ((255 + (color - 255) * alpha) + 0.5f).toInt()
}
