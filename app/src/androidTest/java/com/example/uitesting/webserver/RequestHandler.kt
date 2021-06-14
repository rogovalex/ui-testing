package com.example.uitesting.webserver

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class RequestHandler(
    private val matcher: RequestMatcher,
    private val response: MockResponse,
    private val expectedRequest: ExpectedRequest
) {

    fun handle(request: RecordedRequest): MockResponse? {
        return if (matcher.match(request)) {
            expectedRequest.setOccured()
            response
        } else {
            null
        }
    }
}
