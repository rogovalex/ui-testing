package com.example.uitesting.tests

import com.example.uitesting.webserver.DispatcherRegistry
import com.example.uitesting.webserver.request
import com.example.uitesting.webserver.response

fun DispatcherRegistry.registerPasswordCheck(password: String, result: String) {
    register(
        request {
            path("password/check")
            body("{\"password\": \"$password\"}")
        },
        response {
            setBody("{\"result\": \"$result\"}")
        }
    )
}
