package com.example.uitesting.domain

import rx.Single

interface PasswordRepository {

    fun check(password: String): Single<PasswordStrength>
}
