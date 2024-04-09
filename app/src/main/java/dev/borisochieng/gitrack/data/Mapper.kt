package dev.borisochieng.gitrack.data

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.UserQuery
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.SingleIssue
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

fun UserQuery.Viewer.toSimpleUser(): User {
    return User(username = login)
}

fun UserRepositoriesQuery.Data.toSimpleRepository(): List<Repository> {
    return user?.repositories?.nodes?.map { node ->
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
            openedAt = "Opened ${formatCreatedAt( it.createdAt)}",
            username = it.author?.login ?: "Unknown author",
            commentCount = it.comments.totalCount,
            number = it.number,
            labels = it.labels?.nodes?.mapNotNull { label ->
                label?.name
                label?.color
            } ?: emptyList()
        )
    } ?: mutableListOf<Issue>()
}

fun SingleIssueQuery.Data.toSimpleSingleIssue(): SingleIssue? {
    return repository?.issue?.let { issueResponse ->
        SingleIssue(
            title = issueResponse.title,
            createdAt = "Opened ${formatCreatedAt( issueResponse.createdAt)}",
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

private fun formatCreatedAt(createdAt: Any): String {
    val dateString = createdAt.toString()
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z", Locale.getDefault())
    val outPutFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date = inputFormat.parse(dateString)

    return outPutFormat.format(date)
}
