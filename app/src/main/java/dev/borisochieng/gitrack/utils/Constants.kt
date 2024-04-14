package dev.borisochieng.gitrack.utils

import dev.borisochieng.gitrack.BuildConfig

object Constants {
    const val GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token/"
    const val GITHUB_BASE_URL = "https://api.github.com/graphql"
    const val GITHUB_AUTH_URL = "https://github.com/login/oauth/authorize"
    const val CLIENT_ID = BuildConfig.CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
}