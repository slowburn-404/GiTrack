package dev.borisochieng.gitrack

import android.app.Application
import android.content.SharedPreferences
import dev.borisochieng.gitrack.data.remote.Apollo
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository
import dev.borisochieng.gitrack.data.repositories.AuthRepository
import dev.borisochieng.gitrack.data.GitHubAuthServiceImpl
import dev.borisochieng.gitrack.data.GitHubServiceImpl
import dev.borisochieng.gitrack.data.remote.RetrofitClient
import dev.borisochieng.gitrack.utils.AccessTokenManager

class GitTrackApplication : Application() {

    private lateinit var gitHubServiceImpl: GitHubServiceImpl
    private lateinit var authServiceImpl: GitHubAuthServiceImpl
    private lateinit var accessTokenObserver: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate() {
        super.onCreate()
        //listen for access token changes and initialize it with apollo
        authServiceImpl = GitHubAuthServiceImpl(RetrofitClient.instance)

        accessTokenObserver =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == AccessTokenManager.KEY_ACCESS_TOKEN) {
                    initializeGitrackRepository()
                }
            }
        AccessTokenManager.getSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(accessTokenObserver)
        initializeGitrackRepository()
    }

    val authRepository by lazy { AuthRepository(authServiceImpl) }

    val gitTrackRepository: GitTrackRepository by lazy {
        val accessToken = AccessTokenManager.getAccessToken(this)
        Apollo.accessToken = accessToken
        gitHubServiceImpl = GitHubServiceImpl(Apollo.instance)
        GitTrackRepository(gitHubServiceImpl)
    }

    private fun initializeGitrackRepository() {
        val accessToken = AccessTokenManager.getAccessToken(this)
        accessToken?.let {
            gitTrackRepository
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        AccessTokenManager.getSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(accessTokenObserver)
    }
}