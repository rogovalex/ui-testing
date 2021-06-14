package com.example.uitesting.inject

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RealNetworkModule {
    @Provides
    @Named(NetworkModule.API_BASE_URL)
    fun provideApiBaseUrl(): String {
        return "https://password.check/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
