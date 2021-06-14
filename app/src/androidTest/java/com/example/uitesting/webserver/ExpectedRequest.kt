package com.example.uitesting.webserver

import com.example.uitesting.allure.step

class ExpectedRequest(private val description: String) {

    @Volatile
    private var occured = false

    fun setOccured() {
        occured = true
    }

    fun isOccured() {
        step("Проверяем, что произошел сетевой запрос '$description'") {
            check(occured) { "Expected network request has not occured yet" }
        }
    }
}
