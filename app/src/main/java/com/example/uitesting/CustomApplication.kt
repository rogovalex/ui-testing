package com.example.uitesting

import android.app.Application
import com.example.uitesting.inject.ApplicationComponent
import com.example.uitesting.inject.DaggerApplicationComponent

open class CustomApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = createApplicationComponent()
    }

    protected open fun createApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .build()
    }
}
