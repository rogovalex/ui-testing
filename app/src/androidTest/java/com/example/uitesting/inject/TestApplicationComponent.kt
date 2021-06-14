package com.example.uitesting.inject

import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Component(modules = [BindingModule::class, FakeNetworkModule::class])
@Singleton
interface TestApplicationComponent : ApplicationComponent {

    fun getOkHttpClient(): OkHttpClient
}
