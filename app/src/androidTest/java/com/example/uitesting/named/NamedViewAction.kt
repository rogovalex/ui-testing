package com.example.uitesting.named

import androidx.test.espresso.ViewAction

class NamedViewAction(
    private val name: String,
    private val viewName: String,
    private val action: ViewAction
) : ViewAction by action {

    override fun toString(): String {
        return "$name '$viewName'"
    }
}
