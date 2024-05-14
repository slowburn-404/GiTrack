package dev.borisochieng.gitrack.data.repositories

import dev.borisochieng.gitrack.data.remote.GithubService
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.domain.models.Issue
import dev.borisochieng.gitrack.domain.models.Repository
import dev.borisochieng.gitrack.domain.models.RepositorySearchResult
import dev.borisochieng.gitrack.domain.models.SingleIssue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitTrackRepository(
    private val githubService: GithubService
) {
    suspend fun getUser(): User = withContext(Dispatchers.IO){
        githubService.getUser()
    }

    suspend fun getRepositories(
        username: String
    ): List<Repository>? = withContext(Dispatchers.IO) {
        githubService.getUserRepositories(username)
    }

    suspend fun getRepositoryIssues(
        repositoryName: String,
        repositoryOwner: String,
    ): List<Issue>? = withContext(Dispatchers.IO) {
        githubService.getRepositoryIssue(repositoryName, repositoryOwner)
    }

    suspend fun getSingleIssue(
        username: String, owner: String, number: Int
    ): SingleIssue = withContext(Dispatchers.IO) {
            githubService.getSingleIssue(username, owner, number)
        }

    suspend fun searchPublicRepositories(
        query: String
    ): List<RepositorySearchResult>? = withContext(Dispatchers.IO) {
        githubService.searchPublicRepositories(query)
    }
}