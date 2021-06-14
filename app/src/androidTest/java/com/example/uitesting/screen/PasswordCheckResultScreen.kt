package com.example.uitesting.screen

import com.example.uitesting.named.PasswordCheckResultLookup

class PasswordCheckResultScreen : Screen<PasswordCheckResultScreen>() {

    override val label: String
        get() = "На экране Результат проверки пароля"

    val resultView = TTextView(PasswordCheckResultLookup.matchesResultView())
}
