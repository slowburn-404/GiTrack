package dev.borisochieng.gitrack.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dev.borisochieng.gitrack.utils.Constants.GITHUB_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Apollo {
    var accessToken: String? = null

    val instance: ApolloClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                accessToken?.let {
                    requestBuilder.addHeader("Authorization", "bearer $it")
                }
                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

        ApolloClient.Builder()
            .serverUrl(GITHUB_BASE_URL)
            .okHttpClient(httpClient)
            .build()
    }
}