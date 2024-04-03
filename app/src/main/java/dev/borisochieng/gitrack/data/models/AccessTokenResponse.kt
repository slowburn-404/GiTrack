package dev.borisochieng.gitrack.data.models

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String
)
