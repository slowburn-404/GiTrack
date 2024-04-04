package dev.borisochieng.gitrack.data

import dev.borisochieng.gitrack.domain.GithubService
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.SingleIssue

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
        repositoryName: String,
        repositoryOwner: String,
    ): List<Issue> =
        apiService.getRepositoryIssue(repositoryName, repositoryOwner)



    suspend fun getSingleIssue(
        username: String,
        owner: String,
        number: Int,
        first: Int
    ): SingleIssue =
        apiService.getSingleIssue(username, owner, number, first)



}