package dev.borisochieng.gitrack.domain

import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.SingleIssue

interface GithubService {

    suspend fun getUser(): User

    suspend fun getUserRepositories(username: String): List<Repository>

    suspend fun getRepositoryIssue(): List<Issue>

    suspend fun getSingleIssue(): SingleIssue
}