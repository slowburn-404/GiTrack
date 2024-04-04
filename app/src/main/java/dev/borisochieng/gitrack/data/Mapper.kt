package dev.borisochieng.gitrack.data

import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.UserQuery
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.SingleIssue

fun UserQuery.Viewer.toSimpleUser(): User {
    return User(username = login)
}

fun UserRepositoriesQuery.Data.toSimpleRepository(): List<Repository> {
    return user?.repositories?.nodes?.map {node ->
        Repository(
            id = node!!.id,
            title = node.name,
            desc = node.description ?: "No description",
            starCount = node.stargazerCount,
            issueCount = node.issues.totalCount,
            labels = node.labels?.nodes?.mapNotNull { label ->
                label?.name
            } ?: emptyList()
        )
    } ?: emptyList()
}

fun RepositoryIssuesQuery.Data.toSimpleIssue(): List<Issue> {
    return repository?.issues?.nodes?.map {
        Issue(
            issueTitle = it!!.title,
            issueStatus = it.state.toString(),
            openedAt = it.createdAt.toString(),
            username = it.author?.login ?: "Unknown author",
            commentCount = it.comments.totalCount,
            number = it.number,
            labels = it.labels?.nodes?.mapNotNull { label ->
                label?.name
                label?.color
            } ?: emptyList()
        )
    } ?: emptyList()
}

fun SingleIssueQuery.Data.toSimpleSingleIssue(): SingleIssue? {
    return repository?.issue?.let {issueResponse ->
        SingleIssue(
            title = issueResponse.title,
            createdAt = issueResponse.createdAt.toString(),
            status = issueResponse.state.toString(),
            description = issueResponse.body,
            author = issueResponse.author!!.login,
            labels = issueResponse.labels?.nodes?.mapNotNull { label ->
                label?.name
                label?.color
            } ?: emptyList()
        )
    }
}
