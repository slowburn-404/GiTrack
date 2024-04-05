package dev.borisochieng.gitrack

import android.app.Application
import dev.borisochieng.gitrack.data.Apollo
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.data.GithubClient
import dev.borisochieng.gitrack.utils.AccessTokenManager

class GitTrackApplication: Application() {

    private lateinit var githubClient: GithubClient

    override fun onCreate() {
        super.onCreate()
        //get access token and initialize it with apollo
        Apollo.accessToken = AccessTokenManager.getAccessToken(this)
        githubClient = GithubClient(Apollo.instance)
    }

    val gitTrackRepository by lazy { GitTrackRepository(githubClient) }
}