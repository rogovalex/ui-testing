package com.example.uitesting.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.*

class DispatcherRegistry : Dispatcher() {

    private val registry = LinkedList<RequestHandler>()

    override fun dispatch(request: RecordedRequest): MockResponse {
        synchronized(registry) {
            registry.forEach { handler ->
                val response = handler.handle(request)
                if (response != null) {
                    registry.remove(handler)
                    return response
                }
            }
        }
        return error()
    }

    fun register(
        matcher: RequestMatcher,
        response: MockResponse = error()
    ): ExpectedRequest {
        val expectedRequest = ExpectedRequest(matcher.description)
        synchronized(registry) {
            registry.add(RequestHandler(matcher, response, expectedRequest))
        }
        return expectedRequest
    }
}
