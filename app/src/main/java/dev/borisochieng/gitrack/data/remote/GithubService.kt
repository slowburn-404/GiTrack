package dev.borisochieng.gitrack.data.remote

import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.domain.models.Issue
import dev.borisochieng.gitrack.domain.models.Repository
import dev.borisochieng.gitrack.domain.models.RepositorySearchResult
import dev.borisochieng.gitrack.domain.models.SingleIssue

interface GithubService {

    suspend fun getUser(): User

    suspend fun getUserRepositories(username: String): List<Repository>

    suspend fun getRepositoryIssue(repositoryName: String, repositoryOwner: String): List<Issue>

    suspend fun getSingleIssue(name: String, owner: String, number: Int): SingleIssue

    suspend fun searchPublicRepositories(query: String): List<RepositorySearchResult>
}