package dev.borisochieng.gitrack.data.mapper

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import dev.borisochieng.RepositoryIssuesQuery
import dev.borisochieng.SearchPublicRepositoryQuery
import dev.borisochieng.SingleIssueQuery
import dev.borisochieng.UserQuery
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.domain.models.Issue
import dev.borisochieng.gitrack.domain.models.Repository
import dev.borisochieng.gitrack.domain.models.RepositorySearchResult
import dev.borisochieng.gitrack.domain.models.SingleIssue
import java.util.Locale

// Maps API response to domain level objects

fun UserQuery.Viewer.toSimpleUser(): User {
    return User(username = login)
}

fun UserRepositoriesQuery.Data.toSimpleRepository(): List<Repository> {
    return user?.repositories?.nodes?.mapNotNull { node ->
        Repository(databaseId = node!!.databaseId!!,
            name = node.name,
            owner = node.owner.login,
            desc = node.description ?: "No description",
            starCount = node.stargazerCount,
            issueCount = node.issues.totalCount,
            createdAt = formatCreatedAt(node.createdAt),
            languages = node.languages?.nodes?.mapNotNull { language ->
                language?.name
            } ?: emptyList())
    } ?: emptyList()
}

fun RepositoryIssuesQuery.Data.toSimpleIssue(): List<Issue> {
    return repository?.issues?.nodes?.map { issue ->
        Issue(
            issueTitle = issue!!.title,
            issueStatus = issue.state.toString(),
            openedAt = "Opened ${formatCreatedAt(issue.createdAt)}",
            author = issue.author?.login ?: "Unknown author",
            commentCount = issue.comments.totalCount,
            number = issue.number,
            labels = issue.labels?.nodes?.mapNotNull { label ->
                label?.name
            }?.toSet() ?: emptySet()
        )
    } ?: emptyList()
}

fun SingleIssueQuery.Data.toSimpleSingleIssue(): SingleIssue? {
    return repository?.issue?.let { issueResponse ->
        SingleIssue(title = issueResponse.title,
            createdAt = "Opened ${formatCreatedAt(issueResponse.createdAt)}",
            status = issueResponse.state.toString(),
            description = issueResponse.body,
            author = issueResponse.author!!.login,
            labels = issueResponse.labels?.nodes?.mapNotNull { label ->
                label?.name
            } ?: emptyList())
    }
}

fun SearchPublicRepositoryQuery.Data.toSimpleRepositorySearchResult(): List<RepositorySearchResult> {
    return search.edges?.mapNotNull { edge ->
        edge?.node?.onRepository?.let {
            RepositorySearchResult(
                databaseId = it.databaseId!!,
                repoName = it.name,
                repoOwner = it.owner.login,
                repoDescription = it.description ?: "No description",
                starCount = it.stargazerCount
            )
        }
    }?: emptyList()
}

private fun formatCreatedAt(createdAt: Any): String {
    val dateString = createdAt.toString()
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z", Locale.getDefault())
    val outPutFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT+3:00")
    val date = inputFormat.parse(dateString)

    return outPutFormat.format(date)
}
