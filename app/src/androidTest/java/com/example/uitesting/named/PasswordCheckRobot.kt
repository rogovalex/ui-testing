package com.example.uitesting.named

import com.example.uitesting.allure.step

fun passwordCheck(func: PasswordCheckRobot.() -> Unit): PasswordCheckRobot {
    return step("На экране Проверка пароля") {
        PasswordCheckRobot().apply(func)
    }
}

class PasswordCheckRobot : Robot<PasswordCheckLookup>(PasswordCheckLookup) {

    fun typePassword(password: String) {
        typeText(lookup.matchesPasswordView(), password)
    }

    fun clickCheckButton() {
        click(lookup.matchesCheckButton())
    }
}
