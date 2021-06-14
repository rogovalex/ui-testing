package com.example.uitesting.webserver

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.JsonParser
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import java.util.*

class RequestMatcher private constructor(
    private val matchers: List<Matcher>,
    val description: String
) {

    fun match(request: RecordedRequest): Boolean {
        return matchers.all { it.match(request) }
    }

    interface Matcher {

        fun match(request: RecordedRequest): Boolean
    }

    private class MethodMatcher(private val method: String) : Matcher {

        override fun match(request: RecordedRequest): Boolean {
            return request.method == method
        }
    }

    private class BodyMatcher(private val body: String) : Matcher {

        override fun match(request: RecordedRequest): Boolean {
            val requestJson = JsonParser.parseString(request.body.readUtf8())
            val expectedJson = JsonParser.parseString(body)
            return requestJson == expectedJson
        }
    }

    private class PathMatcher(path: String) : Matcher {

        private val path = "/$path"

        override fun match(request: RecordedRequest) = request.requirePath().startsWith(path)
    }

    private class QueryMatcher(key: String, value: String) : Matcher {

        private val substring = "$key=$value"

        override fun match(request: RecordedRequest) = request.requirePath().contains(substring)
    }

    private class QueryKeyMatcher(key: String) : Matcher {

        private val substring = "$key="

        override fun match(request: RecordedRequest) = request.requirePath().contains(substring)
    }

    private class ExcludedQueryKeyMatcher(key: String) : Matcher {

        private val substring = "$key="

        override fun match(request: RecordedRequest) = !request.requirePath().contains(substring)
    }

    class Builder {

        private var method: String? = null
        private var path: String = ""
        private var body: String? = null
        private val queryParams = mutableMapOf<String, String>()
        private val queryParamKeys = mutableSetOf<String>()
        private val excludedQueryParamKeys = mutableSetOf<String>()

        fun method(method: String) {
            this.method = method
        }

        fun path(prefix: String) {
            path = prefix
        }

        fun body(body: String) {
            this.body = body
        }

        fun assetBody(fileName: String) {
            body(
                Buffer().readFrom(
                    InstrumentationRegistry.getInstrumentation()
                        .context.resources.assets.open(fileName)
                ).readUtf8()
            )
        }

        fun queryParam(key: String, value: String) {
            queryParams[key] = value
        }

        fun queryParamKey(key: String) {
            queryParamKeys.add(key)
        }

        fun excludeQueryParamKey(key: String) {
            excludedQueryParamKeys.add(key)
        }

        fun build(): RequestMatcher {
            val matchers = LinkedList<Matcher>()
            val descriptionBuilder = StringBuilder()
            method?.let { value ->
                matchers.add(MethodMatcher(value))
                descriptionBuilder.append(value)
            }
            matchers.add(PathMatcher(path))
            descriptionBuilder.appendWithDelimiter(path, ", ")
            for ((key, value) in queryParams) {
                matchers.add(QueryMatcher(key, value))
                descriptionBuilder.appendWithDelimiter(key, " ").append('=').append(value)
            }
            for (key in queryParamKeys) {
                matchers.add(QueryKeyMatcher(key))
                descriptionBuilder.appendWithDelimiter("+", " ").append(key)
            }
            for (key in excludedQueryParamKeys) {
                matchers.add(ExcludedQueryKeyMatcher(key))
                descriptionBuilder.appendWithDelimiter("-", " ").append(key)
            }
            body?.let { value ->
                matchers.add(BodyMatcher(value))
                descriptionBuilder.appendWithDelimiter(value, "\n")
            }

            return RequestMatcher(matchers, descriptionBuilder.toString())
        }

        private fun StringBuilder.appendWithDelimiter(
            str: String,
            delimiter: String
        ): StringBuilder {
            if (isNotEmpty()) {
                append(delimiter)
            }
            return append(str)
        }
    }
}
