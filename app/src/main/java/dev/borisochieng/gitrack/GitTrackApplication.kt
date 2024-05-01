package dev.borisochieng.gitrack

import android.app.Application
import android.content.SharedPreferences
import dev.borisochieng.gitrack.data.Apollo
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.data.GithubAuthRepository
import dev.borisochieng.gitrack.data.GithubAuthServiceImpl
import dev.borisochieng.gitrack.data.GithubServiceImpl
import dev.borisochieng.gitrack.data.RetrofitClient
import dev.borisochieng.gitrack.utils.AccessTokenManager

class GitTrackApplication : Application() {

    private lateinit var githubServiceImpl: GithubServiceImpl
    private lateinit var authServiceImpl: GithubAuthServiceImpl
    private lateinit var accessTokenObserver: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate() {
        super.onCreate()
        //listen for access token changes and initialize it with apollo
        authServiceImpl = GithubAuthServiceImpl(RetrofitClient.instance)

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

    val authRepository by lazy { GithubAuthRepository(authServiceImpl) }

    val gitTrackRepository: GitTrackRepository by lazy {
        val accessToken = AccessTokenManager.getAccessToken(this)
        Apollo.accessToken = accessToken
        githubServiceImpl = GithubServiceImpl(Apollo.instance)
        GitTrackRepository(githubServiceImpl)
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