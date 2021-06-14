package com.example.uitesting.inject

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.tls.internal.TlsUtil
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class FakeNetworkModule {
    @Provides
    @Named(NetworkModule.API_BASE_URL)
    fun provideApiBaseUrl(): String {
        return "https://localhost:8080/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .sslSocketFactory(
                TlsUtil.localhost().sslSocketFactory(),
                Platform.get().platformTrustManager()
            )
            .build()
    }
}
