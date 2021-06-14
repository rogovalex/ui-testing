package com.example.uitesting.inject

import com.example.uitesting.data.Api
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient, @Named(API_BASE_URL) baseUrl: String): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
            .create(Api::class.java)
    }

    companion object {
        const val API_BASE_URL = "api_base_url"
    }
}
