package com.example.uitesting.rule

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import com.example.uitesting.CustomTestApplication
import com.jakewharton.espresso.OkHttp3IdlingResource
import org.junit.rules.ExternalResource

class OkHttpIdlingResourceRule : ExternalResource() {

    private val resource: IdlingResource by lazy {
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as CustomTestApplication
        OkHttp3IdlingResource.create("okhttp", app.okHttpClient)
    }

    override fun before() {
        IdlingRegistry.getInstance().register(resource)
    }

    override fun after() {
        IdlingRegistry.getInstance().unregister(resource)
    }
}
