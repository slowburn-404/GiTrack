package dev.borisochieng.gitrack.data

import dev.borisochieng.gitrack.data.remote.GitHubAuthService
import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import retrofit2.Call
import retrofit2.Retrofit

class AuthRepositoryImpl(
    private val retrofit: Retrofit
) : GitHubAuthService {
    override fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): Call<AccessTokenResponse> =
        retrofit.create(GitHubAuthService::class.java)
            .getAccessToken(clientId, clientSecret, code)
}