package com.example.uitesting.webserver

import androidx.test.platform.app.InstrumentationRegistry
import com.example.uitesting.CustomTestApplication
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer

fun configureWebServer(init: DispatcherRegistry.() -> Unit) {
    val app = InstrumentationRegistry.getInstrumentation()
        .targetContext.applicationContext as CustomTestApplication
    app.dispatcherRegistry.apply(init)
}

fun request(init: RequestMatcher.Builder.() -> Unit): RequestMatcher {
    return RequestMatcher.Builder().apply(init).build()
}

fun response(init: MockResponse.() -> Unit): MockResponse {
    return MockResponse().apply(init)
}

fun success(): MockResponse {
    return response { }
}

fun error(): MockResponse {
    return response { setResponseCode(400) }
}

fun MockResponse.assetBody(fileName: String): MockResponse {
    setBody(
        Buffer().readFrom(
            InstrumentationRegistry.getInstrumentation().context.resources.assets.open(
                fileName
            )
        )
    )
    return this
}

fun RecordedRequest.requirePath(): String {
    return requireNotNull(path)
}
