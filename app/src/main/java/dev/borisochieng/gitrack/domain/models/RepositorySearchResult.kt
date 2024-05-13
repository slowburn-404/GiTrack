package dev.borisochieng.gitrack.domain.models

data class RepositorySearchResult(
    val databaseId: Int,
    val repoName: String,
    val repoOwner: String,
    val repoDescription: String,
    val starCount: Int
)
