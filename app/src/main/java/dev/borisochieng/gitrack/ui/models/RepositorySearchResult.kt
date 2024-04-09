package dev.borisochieng.gitrack.ui.models

data class RepositorySearchResult(
    val repoId: String,
    val repoName: String,
    val repoOwner: String,
    val repoDescription: String,
    val starCount: Int
)
