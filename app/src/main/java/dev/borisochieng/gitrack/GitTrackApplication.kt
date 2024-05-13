package dev.borisochieng.gitrack

import android.app.Application
import android.content.SharedPreferences
import dev.borisochieng.gitrack.data.remote.Apollo
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository
import dev.borisochieng.gitrack.data.repositories.AuthRepository
import dev.borisochieng.gitrack.data.AuthRepositoryImpl
import dev.borisochieng.gitrack.data.GitTrackRepositoryImpl
import dev.borisochieng.gitrack.data.remote.RetrofitClient
import dev.borisochieng.gitrack.utils.AccessTokenManager

class GitTrackApplication : Application() {

    private lateinit var gitTrackRepositoryImpl: GitTrackRepositoryImpl
    private lateinit var authServiceImpl: AuthRepositoryImpl
    private lateinit var accessTokenObserver: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate() {
        super.onCreate()
        //listen for access token changes and initialize it with apollo
        authServiceImpl = AuthRepositoryImpl(RetrofitClient.instance)

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
        gitTrackRepositoryImpl = GitTrackRepositoryImpl(Apollo.instance)
        GitTrackRepository(gitTrackRepositoryImpl)
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