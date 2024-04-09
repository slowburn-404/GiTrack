package dev.borisochieng.gitrack

import android.app.Application
import dev.borisochieng.gitrack.data.Apollo
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.data.GithubServiceImpl
import dev.borisochieng.gitrack.utils.AccessTokenManager

class GitTrackApplication: Application() {

    private lateinit var githubServiceImpl: GithubServiceImpl

    override fun onCreate() {
        super.onCreate()
        //get access token and initialize it with apollo
        Apollo.accessToken = AccessTokenManager.getAccessToken(this)
        githubServiceImpl = GithubServiceImpl(Apollo.instance)
    }

    val gitTrackRepository by lazy { GitTrackRepository(githubServiceImpl) }
}