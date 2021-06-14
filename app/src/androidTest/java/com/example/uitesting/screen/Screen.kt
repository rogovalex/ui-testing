package com.example.uitesting.screen

import androidx.test.espresso.Espresso
import com.example.uitesting.named.NamedViewMatcher.Companion.isRoot
import com.example.uitesting.allure.step
import com.example.uitesting.named.NamedViewMatcher

open class Screen<out T : Screen<T>> {

    open val label: String
        get() = "На ${this.javaClass.simpleName}"

    val root: TView = TView(NamedViewMatcher("Корневой элемент", isRoot()))

    fun pressBack() {
        step("Нажимаем на кнопку назад") {
            Espresso.pressBack()
        }
    }

    fun closeKeyboard() {
        step("Скрываем клавиатуру") {
            Espresso.closeSoftKeyboard()
        }
    }

    fun <T> T.waitUntil(
        interval: Long = 500L,
        maxAttempts: Int = 20,
        completionBlock: T.() -> Unit
    ): T {
        com.example.uitesting.utils.waitUntil(interval, maxAttempts) {
            completionBlock(this)
        }
        return this
    }

    fun waitUntil(
        interval: Long = 500L,
        maxAttempts: Int = 20,
        completionBlock: () -> Unit
    ) {
        com.example.uitesting.utils.waitUntil(interval, maxAttempts, completionBlock)
    }

    companion object {

        inline fun <reified T : Screen<T>> onScreen(noinline function: T.() -> Unit): T {
            return T::class.java
                .newInstance()
                .apply {
                    val screen = this
                    step(label) {
                        function(screen)
                    }
                }
        }
    }
}
