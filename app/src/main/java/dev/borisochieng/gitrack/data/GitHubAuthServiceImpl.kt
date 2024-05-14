package dev.borisochieng.gitrack.data

import dev.borisochieng.gitrack.data.remote.GitHubAuthService
import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import retrofit2.Retrofit

class GitHubAuthServiceImpl(
    private val retrofit: Retrofit
) : GitHubAuthService {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): AccessTokenResponse =
        retrofit.create(GitHubAuthService::class.java)
            .getAccessToken(clientId, clientSecret, code)
}