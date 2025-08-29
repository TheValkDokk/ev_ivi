package com.evn.ev_ivi.core.services

import android.content.SharedPreferences
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthApi
import com.evn.ev_ivi.features.map.data.datasource.remote.MapSearchApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "https://api.evnlogistics.com/api/"

    fun getOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        val tokenInterceptor = Interceptor { chain ->
            val token = sharedPreferences.getString("auth_token", null)
            val request = chain.request().newBuilder().apply {
                if (token != null) {
                    addHeader("Authorization", "Token $token")
                }
            }.build()
            chain.proceed(request)
        }
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit(sharedPreferences: SharedPreferences): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(sharedPreferences))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>, sharedPreferences: SharedPreferences): T {
        return getRetrofit(sharedPreferences).create(serviceClass)
    }
    fun mapApi(sharedPreferences: SharedPreferences): MapSearchApi {
        return createService(MapSearchApi::class.java, sharedPreferences)
    }

    fun authApi(sharedPreferences: SharedPreferences): AuthApi {
        return createService(AuthApi::class.java, sharedPreferences)
    }
}