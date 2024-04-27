package dev.borisochieng.gitrack.presentation.models

data class Repository(
    val databaseId: Int,
    val name: String,
    val owner: String,
    val desc: String?,
    val starCount: Int?,
    val issueCount: Int?,
    val createdAt: String,
    val languages: List<String>?
)