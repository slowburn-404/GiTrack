package dev.borisochieng.gitrack.data

import com.apollographql.apollo3.ApolloClient
import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.UserQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.GithubService
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.SingleIssue

class GithubClient(
    private val apolloClient: ApolloClient
) : GithubService {
    override suspend fun getUser(): User {
        return apolloClient
            .query(UserQuery())
            .execute()
            .data
            ?.viewer?.toSimpleUser() ?: User("User does not exist")
    }

    override suspend fun getUserRepositories(username: String): List<Repository> {
        return apolloClient
            .query(UserRepositoriesQuery(username,50))
            .execute()
            .data
            ?.toSimpleRepository()
            ?: emptyList()
    }

    override suspend fun getRepositoryIssue(repositoryName: String, repositoryOwner: String): List<Issue> {
        return apolloClient
            .query(RepositoryIssuesQuery(repositoryName, repositoryOwner, 50))
            .execute()
            .data
            ?.toSimpleIssue() ?: emptyList()

    }

    override suspend fun getSingleIssue(name: String, owner: String, number: Int): SingleIssue {
        return apolloClient
            .query(SingleIssueQuery(name, owner, number, 50))
            .execute()
            .data
            ?.toSimpleSingleIssue() ?: SingleIssue("Title not found", "Unknown date", "Unknown status", "Unknown description", "Unknown author", emptyList())
    }
}