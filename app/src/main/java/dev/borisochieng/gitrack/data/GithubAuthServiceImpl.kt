package dev.borisochieng.gitrack.data

import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import retrofit2.Call
import retrofit2.Retrofit

class GithubAuthServiceImpl(
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