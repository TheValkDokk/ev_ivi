package com.evn.ev_ivi.core.services

import com.evn.ev_ivi.core.common.domain.repository.AuthRepository
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthApi
import com.evn.ev_ivi.features.map.data.datasource.remote.MapSearchApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "https://api.evnlogistics.com/api/"

    fun getOkHttpClient(authRepo: AuthRepository): OkHttpClient {
        val tokenInterceptor = Interceptor { chain ->
            val token = authRepo.getToken()
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

    private fun getRetrofit(authRepo: AuthRepository): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(authRepo))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>, authRepo: AuthRepository): T {
        return getRetrofit(authRepo).create(serviceClass)
    }
    fun mapApi(authRepo: AuthRepository): MapSearchApi {
        return createService(MapSearchApi::class.java, authRepo)
    }

    fun authApi(authRepo: AuthRepository): AuthApi {
        return createService(AuthApi::class.java, authRepo)
    }
}