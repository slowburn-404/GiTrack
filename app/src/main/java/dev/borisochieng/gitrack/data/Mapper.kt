package dev.borisochieng.gitrack.data

import dev.borisochieng.UserQuery
import dev.borisochieng.UserRepositoriesQuery
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Repository

fun UserQuery.Viewer.toSimpleUser(): User {
    return User(username = login)
}

fun UserRepositoriesQuery.Data.toSimpleRepository(): List<Repository> {
    return user?.repositories?.nodes?.map {node ->
        Repository(
            id = node!!.id,
            title = node.name,
            desc = node.description ?: "No description",
            starCount = node.stargazerCount ?: 0,
            issueCount = node.issues.totalCount,
            labels = node.labels?.nodes?.mapNotNull { label ->
                label?.name
            } ?: emptyList()
        )
    } ?: emptyList()
}