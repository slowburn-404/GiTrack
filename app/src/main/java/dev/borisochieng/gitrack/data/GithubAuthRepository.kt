package dev.borisochieng.gitrack.data

class GithubAuthRepository(private val authService: GitHubAuthService) {
    fun getAccessToken(clientId: String, clientSecret: String, code: String) =
        authService.getAccessToken(clientId, clientSecret, code)
}