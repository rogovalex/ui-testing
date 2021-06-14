package com.example.uitesting.data

import retrofit2.http.Body
import retrofit2.http.POST
import rx.Single

interface Api {

    @POST("password/check")
    fun checkPassword(@Body body: PasswordCheckBody): Single<PasswordCheckResponse>
}
