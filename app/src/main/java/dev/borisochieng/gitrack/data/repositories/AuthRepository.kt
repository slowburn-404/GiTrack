package dev.borisochieng.gitrack.data.repositories

import dev.borisochieng.gitrack.data.remote.GitHubAuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class AuthRepository(private val authService: GitHubAuthService) {
    suspend fun getAccessToken(clientId: String, clientSecret: String, code: String) =
        withContext(Dispatchers.IO) {
            authService.getAccessToken(clientId, clientSecret, code)
        }
}