package dev.borisochieng.gitrack.data

import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.gitrack.domain.GithubService
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Repository

class GitTrackRepository(
    private val apiService: GithubService
) {
    suspend fun getUser(): User =
        apiService.getUser()


    suspend fun getRepositories(
        username: String
    ): List<Repository> =
        apiService.getUserRepositories(username)


    suspend fun getRepositoryIssues(
        username: String,
        owner: String,
        count: Int
    ): List<RepositoryIssuesQuery> {
        TODO("Not yet implemented")
    }

    suspend fun getSingleIssue(
        username: String,
        owner: String,
        count: Int
    ): SingleIssueQuery {
        TODO("Not yet implemented")
    }


}