package dev.borisochieng.gitrack.data.repositories

import dev.borisochieng.gitrack.data.remote.GitHubAuthService

class AuthRepository(private val authService: GitHubAuthService) {
    fun getAccessToken(clientId: String, clientSecret: String, code: String) =
        authService.getAccessToken(clientId, clientSecret, code)
}