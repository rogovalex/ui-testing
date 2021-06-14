package com.example.uitesting.screen

import com.example.uitesting.named.PasswordCheckLookup

class PasswordCheckScreen : Screen<PasswordCheckScreen>() {

    override val label: String
        get() = "На экране Проверка пароля"

    val passwordView = TEditText(PasswordCheckLookup.matchesPasswordView())
    val checkButton = TView(PasswordCheckLookup.matchesCheckButton())
}
