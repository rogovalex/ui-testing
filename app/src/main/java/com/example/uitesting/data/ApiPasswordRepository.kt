package com.example.uitesting.data

import com.example.uitesting.domain.PasswordRepository
import com.example.uitesting.domain.PasswordStrength
import rx.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiPasswordRepository @Inject constructor(private val api: Api) : PasswordRepository {

    override fun check(password: String): Single<PasswordStrength> {
        return api.checkPassword(PasswordCheckBody(password))
            .map(PasswordCheckResponse::result)
    }
}
