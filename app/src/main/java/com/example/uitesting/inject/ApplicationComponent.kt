package com.example.uitesting.inject

import dagger.Component
import javax.inject.Singleton

@Component(modules = [BindingModule::class, RealNetworkModule::class])
@Singleton
interface ApplicationComponent {

    fun passwordCheckActivityComponent(
        module: PasswordCheckActivityModule
    ): PasswordCheckActivityComponent
}
