package com.example.uitesting

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import io.qameta.allure.android.runners.AllureAndroidJUnitRunner

class CustomTestRunner : AllureAndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun newApplication(
        cl: ClassLoader,
        className: String,
        context: Context
    ): Application {
        return super.newApplication(cl, CustomTestApplication::class.java.name, context)
    }
}
