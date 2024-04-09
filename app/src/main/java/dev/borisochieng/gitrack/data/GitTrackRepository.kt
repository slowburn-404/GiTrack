package dev.borisochieng.gitrack.data

import androidx.annotation.WorkerThread
import dev.borisochieng.gitrack.domain.GithubService
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.RepositorySearchResult
import dev.borisochieng.gitrack.ui.models.SingleIssue
import kotlinx.coroutines.flow.Flow

class GitTrackRepository(
    private val apiService: GithubService
) {
    @WorkerThread
    suspend fun getUser(): User =
        apiService.getUser()

    @WorkerThread
    suspend fun getRepositories(
        username: String
    ): List<Repository> =
        apiService.getUserRepositories(username)

    @WorkerThread
    suspend fun getRepositoryIssues(
        repositoryName: String,
        repositoryOwner: String,
    ): List<Issue> =
        apiService.getRepositoryIssue(repositoryName, repositoryOwner)


    @WorkerThread
    suspend fun getSingleIssue(
        username: String,
        owner: String,
        number: Int
    ): SingleIssue =
        apiService.getSingleIssue(username, owner, number)

    @WorkerThread
    suspend fun searchPublicRepositories(
        query: String
    ): List<RepositorySearchResult> =
        apiService.searchPublicRepositories(query)
}