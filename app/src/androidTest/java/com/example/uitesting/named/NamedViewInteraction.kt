package com.example.uitesting.named

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import com.example.uitesting.allure.step
import com.example.uitesting.screenshot.ScreenshotInteraction
import com.example.uitesting.screenshot.ScreenshotResult
import org.hamcrest.Matcher
import com.example.uitesting.screenshot.takeViewScreenshot as TakeViewScreenshot
import com.example.uitesting.screenshot.takeRecyclerViewScreenshot as TakeRecyclerViewScreenshot
import com.example.uitesting.screenshot.takeRecyclerViewItemsScreenshot as TakeRecyclerViewItemsScreenshot

class NamedViewInteraction private constructor(
    private val name: String,
    private val interaction: ViewInteraction
) {

    fun perform(action: NamedViewAction): NamedViewInteraction {
        step("$action") {
            interaction.perform(action)
        }
        return this
    }

    fun check(assertion: NamedViewAssertion): NamedViewInteraction {
        step("Проверяем, что '$name' $assertion") {
            interaction.check(assertion)
        }
        return this
    }

    fun click(): NamedViewInteraction {
        return perform(NamedViewAction("Тэп на", name, ViewActions.click()))
    }

    fun typeText(text: String, closeKeyboard: Boolean = true): NamedViewInteraction {
        val interaction = perform(
            NamedViewAction("Ввод текста \"$text\" в", name, ViewActions.typeText(text))
        )

        return if (closeKeyboard) {
            interaction.closeKeyboard()
        } else {
            interaction
        }
    }

    fun closeKeyboard(): NamedViewInteraction {
        return perform(
            NamedViewAction(
                "Скрываем клавиатуру",
                "",
                ViewActions.closeSoftKeyboard()
            )
        )
    }

    fun takeViewScreenshot(
        beforeCapture: (UiController, View) -> Unit = { _, _ -> Unit }
    ): ScreenshotInteraction {
        var screenshotResult: ScreenshotResult? = null
        perform(
            NamedViewAction(
                "Делаем скриншот",
                name,
                TakeViewScreenshot(beforeCapture) { result ->
                    screenshotResult = result
                }
            )
        )
        return ScreenshotInteraction(name, checkNotNull(screenshotResult))
    }

    fun takeRecyclerViewScreenshot(
        beforeCapture: (UiController, View) -> Unit = { _, _ -> Unit },
        scrollExtra: Int = 0,
        rootGetter: (View) -> View = { it.rootView }
    ): ScreenshotInteraction {
        var screenshotResult: ScreenshotResult? = null
        perform(
            NamedViewAction(
                "Делаем скриншоты списка",
                name,
                TakeRecyclerViewScreenshot(rootGetter, beforeCapture, scrollExtra) { result ->
                    screenshotResult = result
                }
            )
        )
        return ScreenshotInteraction(name, checkNotNull(screenshotResult))
    }

    fun takeRecyclerViewItemsScreenshot(
        fromMatcher: Matcher<View>,
        count: Int,
        beforeCapture: (UiController, View) -> Unit = { _, _ -> Unit }
    ): ScreenshotInteraction {
        var screenshotResult: ScreenshotResult? = null
        perform(
            NamedViewAction(
                "Делаем скриншот $count элементов списка начиная с '$fromMatcher'",
                name,
                TakeRecyclerViewItemsScreenshot(fromMatcher, count, beforeCapture) { result ->
                    screenshotResult = result
                }
            )
        )
        return ScreenshotInteraction(name, checkNotNull(screenshotResult))
    }

    companion object {

        fun onView(matcher: Matcher<View>): NamedViewInteraction {
            return NamedViewInteraction(matcher.toString(), Espresso.onView(matcher))
        }
    }
}
