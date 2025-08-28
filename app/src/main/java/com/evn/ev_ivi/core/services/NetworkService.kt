package com.evn.ev_ivi.core.services

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { NetworkService() }
}

class NetworkService {
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()


    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.evnlogistics.com/api")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}