package com.example.uitesting

import com.example.uitesting.inject.ApplicationComponent
import com.example.uitesting.inject.DaggerTestApplicationComponent
import com.example.uitesting.webserver.DispatcherRegistry
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.internal.TlsUtil.localhost

class CustomTestApplication : CustomApplication() {
    lateinit var okHttpClient: OkHttpClient
    val dispatcherRegistry = DispatcherRegistry()

    override fun onCreate() {
        super.onCreate()

        val webServer = MockWebServer()
        webServer.useHttps(localhost().sslSocketFactory(), false)
        webServer.dispatcher = dispatcherRegistry
        webServer.start(8080)
    }

    override fun createApplicationComponent(): ApplicationComponent {
        return DaggerTestApplicationComponent.builder()
            .build()
            .also { okHttpClient = it.getOkHttpClient() }
    }
}
