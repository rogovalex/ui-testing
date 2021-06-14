package com.example.uitesting.robot

fun passwordCheck(func: PasswordCheckRobot.() -> Unit): PasswordCheckRobot {
    return PasswordCheckRobot().apply(func)
}

class PasswordCheckRobot : Robot<PasswordCheckLookup>(PasswordCheckLookup) {

    fun typePassword(password: String) {
        typeText(lookup.matchesPasswordView(), password)
    }

    fun clickCheckButton() {
        click(lookup.matchesCheckButton())
    }
}
