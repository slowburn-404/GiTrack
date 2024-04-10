package dev.borisochieng.gitrack.data

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SearchPublicRepositoryQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.UserQuery
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.models.RepositorySearchResult
import dev.borisochieng.gitrack.ui.models.SingleIssue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun UserQuery.Viewer.toSimpleUser(): User {
    return User(username = login)
}

fun UserRepositoriesQuery.Data.toSimpleRepository(): List<Repository> {
    return user?.repositories?.nodes?.mapNotNull { node ->
            Repository(
                databaseId = node!!.databaseId!!,
                name = node.name,
                owner = node.owner.login,
                desc = node.description ?: "No description",
                starCount = node.stargazerCount,
                issueCount = node.issues.totalCount,
                labels = node.labels?.nodes?.mapNotNull { label ->
                    label?.name
                } ?: emptyList()
            )

    }?: emptyList()
}

fun RepositoryIssuesQuery.Data.toSimpleIssue(): List<Issue> {
    return repository?.issues?.nodes?.map {
        Issue(
            databaseId = it!!.databaseId!!,
            issueTitle = it.title,
            issueStatus = it.state.toString(),
            openedAt = "Opened ${formatCreatedAt( it.createdAt)}",
            author = it.author?.login ?: "Unknown author",
            commentCount = it.comments.totalCount,
            number = it.number,
            labels = it.labels?.nodes?.mapNotNull { label ->
                label?.name
                label?.color
            } ?: emptyList()
        )
    }?.sortedByDescending {
        it.number
    } ?: mutableListOf()
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

fun SearchPublicRepositoryQuery.Data.toSimpleRepositorySearchResult() : List<RepositorySearchResult> {
    return search.edges?.mapNotNull { edge->
        edge?.node?.onRepository?.let {
            RepositorySearchResult(
                databaseId = it.databaseId!!,
                repoName= it.name,
                repoOwner = it.owner.login,
                repoDescription = it.description ?: "No description",
                starCount = it.stargazerCount
            )
        }
    }?.sortedByDescending {
        it.starCount
    } ?: emptyList()
}

private fun formatCreatedAt(createdAt: Any): String {
    val dateString = createdAt.toString()
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z", Locale.getDefault())
    val outPutFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT+3:00")
    val date = inputFormat.parse(dateString)

    return outPutFormat.format(date)
}
