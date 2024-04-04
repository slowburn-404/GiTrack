package dev.borisochieng.gitrack.data

import dev.borisochieng.gitrack.utils.Constants.GITHUB_TOKEN_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val instance: Retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader("Connection", "close")
                        .build()
                chain.proceed(request)
            }.build()

        Retrofit.Builder()
            .baseUrl(GITHUB_TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    }
}