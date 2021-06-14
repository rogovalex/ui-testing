package com.example.uitesting.named

import com.example.uitesting.R
import com.example.uitesting.named.NamedViewMatcher.Companion.withId

object PasswordCheckLookup {

    fun matchesPasswordView(): NamedViewMatcher {
        return NamedViewMatcher(
            "Поле ввода пароля",
            withId(R.id.passwordView)
        )
    }

    fun matchesCheckButton(): NamedViewMatcher {
        return NamedViewMatcher(
            "Кнопка Проверить",
            withId(R.id.checkButton)
        )
    }
}
