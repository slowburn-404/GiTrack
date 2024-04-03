package dev.borisochieng.gitrack.data.remote

import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import dev.borisochieng.gitrack.utils.Constants.GITHUB_TOKEN_URL
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GitHubAuthService {
    @Headers("Accept: application/json")
    @POST(GITHUB_TOKEN_URL)
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessTokenResponse>
}