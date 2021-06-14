package com.example.uitesting.screenshot

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.runner.screenshot.Screenshot
import org.hamcrest.Matcher

private const val SCREENSHOT_DELAY_MS = 300L

fun takeViewScreenshot(
        beforeCapture: (uiController: UiController, view: View) -> Unit,
        onCapture: (ScreenshotResult) -> Unit
): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "take view screenshot"
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(View::class.java)
        }

        override fun perform(uiController: UiController, view: View) {
            if (view.rootView == view || view is RecyclerView) {
                uiController.loopMainThreadForAtLeast(SCREENSHOT_DELAY_MS)
            }
            beforeCapture.invoke(uiController, view)
            onCapture.invoke(ScreenshotResult.SingleScreenshot(Screenshot.capture(view).bitmap))
        }
    }
}

fun takeRecyclerViewScreenshot(
    rootGetter: (View) -> View,
    beforeCapture: (uiController: UiController, view: View) -> Unit,
    scrollExtra: Int,
    onCapture: (ScreenshotResult) -> Unit,
): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "take recycler view screenshots"
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(RecyclerView::class.java)
        }

        override fun perform(uiController: UiController, view: View) {
            view as RecyclerView
            if (view.canScrollVertically(-1) || view.canScrollHorizontally(-1)) {
                view.scrollToPosition(0)
            }
            uiController.loopMainThreadForAtLeast(SCREENSHOT_DELAY_MS)
            val rootView = rootGetter(view)
            beforeCapture.invoke(uiController, rootView)
            val bitmaps = mutableListOf<Bitmap>()
            bitmaps.add(Screenshot.capture(rootView).bitmap)

            val canScroll: View.(Int) -> Boolean
            val dx: Int
            val dy: Int
            if (view.canScrollVertically(1)) {
                canScroll = View::canScrollVertically
                dx = 0
                dy = view.height + scrollExtra
            } else {
                canScroll = View::canScrollHorizontally
                dx = view.width + scrollExtra
                dy = 0
            }
            while (view.canScroll(1)) {
                view.scrollBy(dx, dy)
                uiController.loopMainThreadUntilIdle()
                beforeCapture.invoke(uiController, rootView)
                bitmaps.add(Screenshot.capture(rootView).bitmap)
            }
            onCapture.invoke(ScreenshotResult.Screenshots(bitmaps))
        }
    }
}

fun takeRecyclerViewItemsScreenshot(
    fromMatcher: Matcher<View>,
    count: Int,
    beforeCapture: (uiController: UiController, view: View) -> Unit,
    onCapture: (ScreenshotResult) -> Unit
): ViewAction {
    val scrollAction = RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(fromMatcher)
    return object : ViewAction {
        override fun getDescription(): String {
            return "take screenshot of $count recycler view items"
        }

        override fun getConstraints(): Matcher<View> {
            return scrollAction.constraints
        }

        override fun perform(uiController: UiController, view: View) {
            view as RecyclerView

            scrollAction.perform(uiController, view)

            var startPosition: Int? = null
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (fromMatcher.matches(child)) {
                    startPosition = view.getChildViewHolder(child).adapterPosition
                    break
                }
            }
            checkNotNull(startPosition)

            val canvas = Canvas()
            val paint = Paint()

            val bitmaps = mutableListOf<Bitmap>()
            for (i in startPosition until startPosition + count) {
                view.scrollToPosition(i)
                uiController.loopMainThreadUntilIdle()
                val viewHolder = checkNotNull(view.findViewHolderForAdapterPosition(i))
                beforeCapture.invoke(uiController, viewHolder.itemView)
                val lp = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
                val bitmap = if (
                    lp.bottomMargin != 0 ||
                    lp.leftMargin != 0 ||
                    lp.rightMargin != 0 ||
                    lp.topMargin != 0
                ) {
                    val capturedBitmap = Screenshot.capture(viewHolder.itemView).bitmap
                    val marginBitmap = Bitmap.createBitmap(
                        capturedBitmap.width + lp.leftMargin + lp.rightMargin,
                        capturedBitmap.height + lp.bottomMargin + lp.topMargin,
                        Bitmap.Config.ARGB_8888
                    )
                    canvas.setBitmap(marginBitmap)
                    canvas.drawBitmap(
                        capturedBitmap,
                        lp.leftMargin.toFloat(),
                        lp.topMargin.toFloat(),
                        paint
                    )
                    capturedBitmap.recycle()
                    marginBitmap
                } else {
                    Screenshot.capture(viewHolder.itemView).bitmap
                }
                bitmaps.add(bitmap)
            }

            val result = Bitmap.createBitmap(
                bitmaps.maxOf { it.width },
                bitmaps.sumBy { it.height },
                Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(result)
            var offset = 0
            bitmaps.forEach { bitmap ->
                canvas.drawBitmap(bitmap, 0f, offset.toFloat(), paint)
                offset += bitmap.height
                bitmap.recycle()
            }

            onCapture.invoke(ScreenshotResult.SingleScreenshot(result))
        }
    }
}
